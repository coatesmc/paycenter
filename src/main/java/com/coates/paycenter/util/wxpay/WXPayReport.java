package com.coates.paycenter.util.wxpay;

import com.coates.paycenter.configuration.WeiXinConfig;
import com.github.wxpay.sdk.WXPayConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 交易保障
 */
public class WXPayReport {

    public static class ReportInfo {

        /**
         * 布尔变量使用int。0为false， 1为true。
         */

        // 基本信息
        private String version = "v0";
        private String sdk = "wxpay java sdk v1.0";
        private String uuid;  // 交易的标识
        private long timestamp;   // 上报时的时间戳，单位秒
        private long elapsedTimeMillis; // 耗时，单位 毫秒

        // 针对主域名
        private String firstDomain;  // 第1次请求的域名
        private boolean primaryDomain; //是否主域名
        private int firstConnectTimeoutMillis;  // 第1次请求设置的连接超时时间，单位 毫秒
        private int firstReadTimeoutMillis;  // 第1次请求设置的读写超时时间，单位 毫秒
        private int firstHasDnsError;  // 第1次请求是否出现dns问题
        private int firstHasConnectTimeout; // 第1次请求是否出现连接超时
        private int firstHasReadTimeout; // 第1次请求是否出现连接超时

        public ReportInfo(String uuid, long timestamp, long elapsedTimeMillis, String firstDomain, boolean primaryDomain, int firstConnectTimeoutMillis, int firstReadTimeoutMillis, boolean firstHasDnsError, boolean firstHasConnectTimeout, boolean firstHasReadTimeout) {
            this.uuid = uuid;
            this.timestamp = timestamp;
            this.elapsedTimeMillis = elapsedTimeMillis;
            this.firstDomain = firstDomain;
            this.primaryDomain = primaryDomain;
            this.firstConnectTimeoutMillis = firstConnectTimeoutMillis;
            this.firstReadTimeoutMillis = firstReadTimeoutMillis;
            this.firstHasDnsError = firstHasDnsError?1:0;
            this.firstHasConnectTimeout = firstHasConnectTimeout?1:0;
            this.firstHasReadTimeout = firstHasReadTimeout?1:0;
         }

        @Override
        public String toString() {
            return "ReportInfo{" +
                    "version='" + version + '\'' +
                    ", sdk='" + sdk + '\'' +
                    ", uuid='" + uuid + '\'' +
                    ", timestamp=" + timestamp +
                    ", elapsedTimeMillis=" + elapsedTimeMillis +
                    ", firstDomain='" + firstDomain + '\'' +
                    ", primaryDomain=" + primaryDomain +
                    ", firstConnectTimeoutMillis=" + firstConnectTimeoutMillis +
                    ", firstReadTimeoutMillis=" + firstReadTimeoutMillis +
                    ", firstHasDnsError=" + firstHasDnsError +
                    ", firstHasConnectTimeout=" + firstHasConnectTimeout +
                    ", firstHasReadTimeout=" + firstHasReadTimeout +
                    '}';
        }

        /**
         * 转换成 csv 格式
         *
         * @return
         */
        public String toLineString(String key) {
            String separator = ",";
            Object[] objects = new Object[] {
                version, sdk, uuid, timestamp, elapsedTimeMillis,
                    firstDomain, primaryDomain, firstConnectTimeoutMillis, firstReadTimeoutMillis,
                    firstHasDnsError, firstHasConnectTimeout, firstHasReadTimeout
            };
            StringBuffer sb = new StringBuffer();
            for(Object obj: objects) {
                sb.append(obj).append(separator);
            }
            try {
                String sign = WXPayUtil.HMACSHA256(sb.toString(), key);
                sb.append(sign);
                return sb.toString();
            }
            catch (Exception ex) {
                return null;
            }

        }

    }

    private static final String REPORT_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //private static final String REPORT_URL = "https://api.mch.weixin.qq.com/sandbox/pay/micropay";

    private static final int DEFAULT_CONNECT_TIMEOUT_MS = 6*1000;
    private static final int DEFAULT_READ_TIMEOUT_MS = 8*1000;

    private LinkedBlockingQueue<String> reportMsgQueue = null;
    private WXPayConfig config;

    public void report(String uuid, long elapsedTimeMillis,
                       String firstDomain, boolean primaryDomain, int firstConnectTimeoutMillis, int firstReadTimeoutMillis,
                       boolean firstHasDnsError, boolean firstHasConnectTimeout, boolean firstHasReadTimeout) {
        long currentTimestamp = WXPayUtil.getCurrentTimestamp();
        ReportInfo reportInfo = new ReportInfo(uuid, currentTimestamp, elapsedTimeMillis,
                firstDomain, primaryDomain, firstConnectTimeoutMillis, firstReadTimeoutMillis,
                firstHasDnsError, firstHasConnectTimeout, firstHasReadTimeout);
        String data = reportInfo.toLineString(config.getKey());
        WXPayUtil.getLogger().info("report {}", data);
        if (data != null) {
            reportMsgQueue.offer(data);
        }
    }

    @Deprecated
    private void reportAsync(final String data) throws Exception {
        new Thread(new Runnable() {
            public void run() {
                try {
                    httpRequest(data, DEFAULT_CONNECT_TIMEOUT_MS, DEFAULT_READ_TIMEOUT_MS);
                }
                catch (Exception ex) {
                    WXPayUtil.getLogger().warn("report fail. reason: {}", ex.getMessage());
                }
            }
        }).start();
    }

    @Deprecated
	public static String reportSync(final String data) throws Exception {
        return httpRequest(data, DEFAULT_CONNECT_TIMEOUT_MS, DEFAULT_READ_TIMEOUT_MS);
    }
    /**
     * http 请求
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @return
     * @throws Exception
     */
    private static String httpRequest(String data, int connectTimeoutMs, int readTimeoutMs) throws Exception{
        BasicHttpClientConnectionManager connManager;
        connManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build(),
                null,
                null,
                null
        );
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

        HttpPost httpPost = new HttpPost(REPORT_URL);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 ");  // TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
        httpPost.setEntity(postEntity);
        
        System.out.println("httpPost:"+httpPost.toString());

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }
    
    //请求器的配置  
    private static RequestConfig requestConfig;  
  
    //HTTP请求器  
    private static CloseableHttpClient httpClient;  

    
    /** 
     * 加载证书 
     * @param path 
     * @throws IOException 
     * @throws KeyStoreException 
     * @throws UnrecoverableKeyException 
     * @throws NoSuchAlgorithmException 
     * @throws KeyManagementException 
     */  
    @SuppressWarnings("deprecation")
	private static void initCert(String path) throws Exception{  
    	WeiXinConfig config = new WeiXinConfig();
    	
        //拼接证书的路径  
        path = path + config.certLocalPath;  
        KeyStore keyStore = KeyStore.getInstance("PKCS12");  
  
        //加载本地的证书进行https加密传输  
        FileInputStream instream = new FileInputStream(new File(path));  
        try {  
            keyStore.load(instream, config.getMchID().toCharArray());  //加载证书密码，默认为商户ID  
        } catch (CertificateException e) {  
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } finally {  
            instream.close();  
        }  
  
        SSLContext sslcontext = SSLContexts.custom()  
                .loadKeyMaterial(keyStore,config.getMchID().toCharArray())       //加载证书密码，默认为商户ID  
                .build();  
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(  
                sslcontext,  
                new String[]{"TLSv1"},  
                null,  
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);  
  
        httpClient = HttpClients.custom()  
                .setSSLSocketFactory(sslsf)  
                .build();  
  
        //根据默认超时限制初始化requestConfig  
        requestConfig = RequestConfig.custom().setSocketTimeout(config.getHttpConnectTimeoutMs()).setConnectTimeout(config.getHttpReadTimeoutMs()).build();  
  
    }  
  
    /** 
     * 通过Https往API post xml数据 
     * @param url   API地址 
     * @param xmlObj   要提交的XML数据对象 
     * @param path    当前目录，用于加载证书 
     * @return 
     * @throws IOException 
     * @throws KeyStoreException 
     * @throws UnrecoverableKeyException 
     * @throws NoSuchAlgorithmException 
     * @throws KeyManagementException 
     */  
    public static String httpsRequest(String url, String xmlObj, String path) throws Exception {  
        //加载证书  
        initCert(path);  
  
        String result = null;  
  
        HttpPost httpPost = new HttpPost(url);  
  
        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别  
        StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");  
        httpPost.addHeader("Content-Type", "text/xml");  
        httpPost.setEntity(postEntity);  
  
        //设置请求器的配置  
        httpPost.setConfig(requestConfig);  
  
        try {  
            HttpResponse response = httpClient.execute(httpPost);  
  
            HttpEntity entity = response.getEntity();  
  
            result = EntityUtils.toString(entity, "UTF-8");  
  
        } catch (ConnectionPoolTimeoutException e) {  
            System.out.println("http get throw ConnectionPoolTimeoutException(wait time out)");  
  
        } catch (ConnectTimeoutException e) {  
            System.out.println("http get throw ConnectTimeoutException");  
  
        } catch (SocketTimeoutException e) {  
             System.out.println("http get throw SocketTimeoutException");  
  
        } catch (Exception e) {  
             System.out.println("http get throw Exception");  
  
        } finally {  
            httpPost.abort();  
        }  
  
        return result;  
    }  

}
