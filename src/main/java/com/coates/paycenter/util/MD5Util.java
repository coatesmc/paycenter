package com.coates.paycenter.util;

import java.security.MessageDigest;

public class MD5Util {
	/**
	 * 利用MD5进行加密 @param str 待加密的字符串 @return 加密后的字符串 @throws
	 * NoSuchAlgorithmException 没有这种产生消息摘要的算法 @throws
	 * UnsupportedEncodingException
	 */

	public static String MD5Encode(String str){
		try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(str.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String strs = Integer.toHexString(number);
                if (strs.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(strs);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (Exception e) {
            return "";
        }
	}
}
