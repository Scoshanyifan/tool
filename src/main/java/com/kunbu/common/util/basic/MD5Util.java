package com.kunbu.common.util.basic;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
     * MD5加密
     *
     * @Author zhouq
     * @Date 2017/10/30 16:22
     */
    public static String encodeMD5(String s) {
        return encodeMD5(s, null);
    }

    /**
     * MD5加盐加密
     */
    public static String encodeMD5(String s, String salt) {
        if (s == null || s.trim().length() == 0) {
            return null;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        md.update(s.getBytes());
        if (salt != null) {
            md.update(salt.getBytes());
        }
        byte[] datas = md.digest();
        // [64, -19, 71, 125, 118, -13, -21, -125, 31, 102, 121, 66, 101, -69, 44, 6]
        System.out.println(Arrays.toString(datas));
        int len = datas.length;
        char str[] = new char[len * 2];
        int k = 0;
        for (int i = 0; i < len; i++) {
            byte byte0 = datas[i];
            // TODO 为了转正 byte:[-127,128]
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * MD5
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

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(getMD5("100160003200410095"));
        System.out.println(encodeMD5("10016", "key"));


    }
}
