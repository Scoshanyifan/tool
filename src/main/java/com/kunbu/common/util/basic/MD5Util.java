package com.kunbu.common.util.basic;

import java.security.MessageDigest;

/**
 * Message-Digest Algorithm 5：信息-摘要算法5
 * <p>
 * 用于确保信息传输完整一致
 * <p>
 * 同类算法：sha-1
 *
 * @project: bucks
 * @author: kunbu
 * @create: 2019-09-04 11:11
 **/
public class MD5Util {

    private static final String CHARSET_UTF8 = "UTF-8";

    /**
     * 得到MD5
     *
     * @param original
     * @return
     * @author kunbu
     * @time 2019/9/4 13:21
     **/
    public static String getMD5(String original) {
        String result = null;
        try {
            //加密算法为MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //信息摘要字节数组
            byte[] bytes = md.digest(original.getBytes(CHARSET_UTF8));
            //byte[]转16进制字符串
            result = byteArr2HexString(bytes);
        } catch (Exception e) {

        }
        return result;
    }

    private static String byteArr2HexString(byte[] byteArr) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < byteArr.length; i++) {
            String hex = Integer.toHexString(byteArr[i] & 0xFF);
            if (hex.length() == 1) {
                builder.append("0");
            }
            builder.append(hex);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(getMD5("123456"));
    }
}
