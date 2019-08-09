package com.coates.paycenter.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import sun.misc.BASE64Decoder;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>Title: RSACoder.java </p>
 * <p>Package com.shenpinkj.RSA </p>
 * <p>Description: TODO(经典的数字签名算法RSA)  </p>
 * <p>Company: www.shenpinkj.cn</p>
 *
 * @author 牟超
 * @version 1.0
 * @date 2017年12月9日下午3:00:14
 */
public class RSACoder {
    //数字签名，密钥算法
    public static final String KEY_ALGORITHM = "RSA";
    // 公钥
    private static String DEFAULT_PUBLIC_KEY = "";
    // 私钥
    private static String DEFAULT_PRIVATE_KEY = "";

    /**
     * 数字签名
     * 签名/验证算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    //公钥
    private static final String PUBLIC_KEY = "RSAPublicKey";

    //私钥
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() throws Exception {
        //将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, getPublicKey(DEFAULT_PUBLIC_KEY));
        keyMap.put(PRIVATE_KEY, getPrivateKey(DEFAULT_PRIVATE_KEY));
        return keyMap;

    }

    /**
     * 创 建 人：牟 超
     * 创建时间：2017年12月9日
     * 方法描述：String 转 公钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 创 建 人：牟 超
     * 创建时间：2017年12月9日
     * 方法描述： String 转私钥串
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 签名
     *
     * @param data待签名数据
     * @param privateKey 密钥
     * @return byte[] 数字签名
     */
    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {

        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //实例化Signature
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        //初始化Signature
        signature.initSign(priKey);
        //更新
        signature.update(data);
        return signature.sign();
    }

    /**
     * 校验数字签名
     *
     * @param data      待校验数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return boolean 校验成功返回true，失败返回false
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
        //转换公钥材料
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //实例化Signature
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        //初始化Signature
        signature.initVerify(pubKey);
        //更新
        signature.update(data);
        //验证
        return signature.verify(sign);
    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }
    /**
     * @param args
     * @throws Exception
     */
/*	public static void main(String[] args) throws Exception {
		String str="afterBalance=999000.00&beforeBalance=1000000.00&busiid=2&dealDate=Sat+Dec+09+15%3A25%3A06+CST+2017&money=1000&receiptAccount=4534564546&receiptAddress=%E5%8F%91%E4%B8%AA%E5%9B%9E%E5%A4%8D%E8%AF%A5%E5%90%8E&receiptName=%E8%B6%85%E8%B4%9F%E8%8D%B7%E5%88%9A&receiptUsername=%E6%9B%B4%E5%A5%BD&serialNumber=671512804306099&";
		//初始化密钥
		Map<String,Object> keyMap=RSACoder.initKey();
		 //公钥  
        byte[] publicKey=RSACoder.getPublicKey(keyMap);  
          
        //私钥  
        byte[] privateKey=RSACoder.getPrivateKey(keyMap);  
        System.out.println("公钥："+Base64.encodeBase64String(publicKey));  
        System.out.println("私钥："+Base64.encodeBase64String(privateKey));  
          
        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");  
        System.out.println("原文:"+str);  
        
        //甲方进行数据的加密  
        byte[] sign=RSACoder.sign(str.getBytes(), privateKey); 
        
        String sis = Base64.encodeBase64String(sign);
        
        
        System.out.println("产生签名："+Base64.encodeBase64String(sign));  
        
        System.out.println("byte[sign]:"+sign);
        System.out.println("byte[sis]:"+Base64.decodeBase64(sis).toString());
        if(sign.equals(Base64.decodeBase64(sis))){
        	System.out.println("byte转换结果为:"+true);
        }else{
        	System.out.println("byte转换结果为:"+false);
        }
        
        //验证签名  
        boolean status=RSACoder.verify(str.getBytes(), publicKey, sis.getBytes());  
        System.out.println("状态："+status);  
          
	}*/

    /**
     * 创 建 人：牟 超
     * 创建时间：2017年12月9日
     * 方法描述：解密 待调试
     *
     * @param sign
     * @return
     */
    public static String decryptionCheck(String str, String sign) {
        //初始化密钥
        Map<String, Object> keyMap;
        //验证签名
        boolean status = false;
        try {
            keyMap = RSACoder.initKey();
            //公钥
            byte[] publicKey = RSACoder.getPublicKey(keyMap);
            System.out.println("公钥：" + Base64.encodeBase64String(publicKey));
            status = RSACoder.verify(str.getBytes(), publicKey, sign.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("状态：" + status);
        return sign;
    }


    /**
     * 创 建 人：牟 超
     * 创建时间：2017年12月9日
     * 方法描述： 加密处理
     *
     * @param str
     * @return
     */
    public static String encrypting(String str) {
        byte[] sign = null;
        try {
            //初始化密钥
            Map<String, Object> keyMap = RSACoder.initKey();
            //私钥
            byte[] privateKey = RSACoder.getPrivateKey(keyMap);
            System.out.println("私钥：" + Base64.encodeBase64String(privateKey));
            sign = RSACoder.sign(str.getBytes(), privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String signs = Base64.encodeBase64String(sign);
        System.out.println("产生签名：" + signs);
        return signs;
    }
}
