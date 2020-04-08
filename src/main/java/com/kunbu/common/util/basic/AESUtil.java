package com.kunbu.common.util.basic;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES对称加密工具类
 *
 * @author: kunbu
 * @create: 2019-11-22 17:28
 **/
public class AESUtil {

    private static final Logger logger = LoggerFactory.getLogger(AESUtil.class);

    /** 字符编码 */
    private static final String DEFAULT_CHARSET = "UTF-8";
    /** 密钥加密算法 */
    private static final String KEY_ALGORITHM = "AES";
    /** 加密算法 */
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    /** key生成种子算法 */
    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";

    /** 秘钥（类似于叮咚，需要128位字符的随机种子） */
    private static final String DEFAULT_SECRET_SEED = "kunbu";
    /** 默认密钥key */
    private static Key DEFAULT_KEY;

    static {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom sr = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
            sr.setSeed(DEFAULT_SECRET_SEED.getBytes(DEFAULT_CHARSET));
            kg.init(128, sr);
            SecretKey secretKey = kg.generateKey();
            DEFAULT_KEY = new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (Exception e) {
            //
        }
    }

    /**
     * 自定义种子加密
     *
     * @param content
     * @param seed
     * @return
     */
    public static String encrypt(String content, String seed) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        if (StringUtils.isBlank(seed)) {
            seed = DEFAULT_SECRET_SEED;
        }
        try {
            byte[] byteContent = content.getBytes(DEFAULT_CHARSET);
            //加密
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(seed));
            byte[] result = cipher.doFinal(byteContent);
            //返回base64后的密文
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            logger.error(">>> AESUtil encrypt error.", e);
            return null;
        }
    }

    /**
     * 自定义key加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String encrypt(String content, Key key) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            if (key == null) {
                key = getSecretKey(DEFAULT_SECRET_SEED);
            }
            byte[] byteContent = content.getBytes(DEFAULT_CHARSET);
            //加密
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            //返回base64后的密文
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            logger.error(">>> AESUtil encrypt error.", e);
            return null;
        }
    }

    /**
     * 自定义解密
     *
     * @param content
     * @param seed
     * @return
     */
    public static String decrypt(String content, String seed) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        if (StringUtils.isBlank(seed)) {
            seed = DEFAULT_SECRET_SEED;
        }
        try {
            byte[] base64ByteArr = Base64.getDecoder().decode(content);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(seed));
            byte[] result = cipher.doFinal(base64ByteArr);

            return new String(result, DEFAULT_CHARSET);
        } catch (Exception e) {
            logger.error(">>> AESUtil decrypt error.", e);
            return null;
        }
    }

    /**
     * 自定义解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, Key key) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            if (key == null) {
                key = getSecretKey(DEFAULT_SECRET_SEED);
            }
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(result, DEFAULT_CHARSET);
        } catch (Exception e) {
            logger.error(">>> AESUtil decrypt error.", e);
            return null;
        }
    }

    /**
     * 生成加密秘钥
     *
     * @param seed
     * @return
     */
    public static Key getSecretKey(String seed) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom sr = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
        sr.setSeed(seed.getBytes());
        kg.init(128, sr);
        SecretKey secretKey = kg.generateKey();
        Key key = new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        return key;
    }

}
