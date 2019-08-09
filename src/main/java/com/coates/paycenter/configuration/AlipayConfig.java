package com.coates.paycenter.configuration;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016083100374900";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ3Tb1KIC1nvinkTHMZX4VltJxYBDEDLJU5WqQdkVfDCwLHq8WWDY/kVLUEEIzagzQ818WyVcrJtTF30KouqpWYhAeUIG2ApkJl/2iMu3UDXl9QwjzwRsbNrswNsRGUb6EnFyEILmfOqKSEdDEfY8jb5cvDyOfxOUsZBV+twXnmjAgMBAAECgYA3V74Qnb4FQnc3L5mGf6h42Eq9b46hZdrmFoNdITT1oQU7HlyZsTIsVN2yj8XdURO3Ar5uyhUXNjZyN713jlZC5TjDe17uMqGLJVdlw3qObY9u9/nxmh0MC15YwwWDgLG5dFDioLhZHIixqp0/pJtj5rig5+pdTLRoWeF5iuWx0QJBANyl2Yz5XYBy8JLMuBLtXWGhZ7ZOjwJ0QYvxRicxj7xxzIx5EB4uiMmweIFjrMhxIQ8VxYuGPkqsaac+LfMbbGsCQQC3HN03cZ55wMTX29iJAG6SP2eduON3AnhGU4LcUDfbKn4uccXpJ0ReUWZ6zwu1yFzLtoCf3iMmdugACpMN5XWpAkANAlpJTYqNwDke7qMLuqL+1p1eylL0OhaDQb55IRrwaWU+AwVIQkYeXiE8v7u4NbNVSFtVVpzlmjTAf5IF+1/xAkEAgwPg9+mQdRbLAJNpI+UuN++ryHrgLWeT/OWFrPKsdGC79akr7L1OuplNVLNNcpxQYOVMtJXw2nWbW2Q1gWzbOQJAN9WNMhQp3byXZKDwBvJ8hMQ8j4eH+0ji57/z8r7fjwMNXItVYqTZ79+LJrYMDSFn/+c54yXNtIXkmD8Ou897Fg==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCd029SiAtZ74p5ExzGV+FZbScWAQxAyyVOVqkHZFXwwsCx6vFlg2P5FS1BBCM2oM0PNfFslXKybUxd9CqLqqVmIQHlCBtgKZCZf9ojLt1A15fUMI88EbGza7MDbERlG+hJxchCC5nzqikhHQxH2PI2+XLw8jn8TlLGQVfrcF55owIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "";

    // 签名方式
    public static String sign_type = "RSA";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String getMerchant_private_key() {
        return merchant_private_key;
    }

    public static void setMerchant_private_key(String merchant_private_key) {
        AlipayConfig.merchant_private_key = merchant_private_key;
    }

    public static String getAlipay_public_key() {
        return alipay_public_key;
    }

    public static void setAlipay_public_key(String alipay_public_key) {
        AlipayConfig.alipay_public_key = alipay_public_key;
    }

    public static String getNotify_url() {
        return notify_url;
    }

    public static void setNotify_url(String notify_url) {
        AlipayConfig.notify_url = notify_url;
    }

    public static String getReturn_url() {
        return return_url;
    }

    public static void setReturn_url(String return_url) {
        AlipayConfig.return_url = return_url;
    }

    public static String getGatewayUrl() {
        return gatewayUrl;
    }

    public static void setGatewayUrl(String gatewayUrl) {
        AlipayConfig.gatewayUrl = gatewayUrl;
    }
}