package com.kunbu.common.util.tool.img.qr;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class QrCodeDTO {

    public static final String FORMAT_PNG = "png";
    public static final String FORMAT_JPG = "jpg";

    private static final int DEFAULT_QR_WIDTH = 200;
    private static final int DEFAULT_QR_HEIGHT = 200;
    private static final int DEFAULT_WORD_HEIGHT = 20;

    /**
     * 宽度
     */
    private int width;
    /**
     * 高度
     */
    private int height;
    /**
     * 文字
     */
    private String word;
    /**
     * 文字高度
     */
    private int wordHeight;
    /**
     * 二维码格式
     */
    private String format;
    /**
     * 二维码内容
     */
    private String qrContent;
    /**
     * logo图片
     */
    private BufferedImage logoImage;

    private Map<EncodeHintType, Object> encodeTypeMap;

    private QrCodeDTO() {}

    public static QrCodeDTO init(String qrContent, String word) {
        return init(qrContent, word, FORMAT_PNG, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT);
    }

    public static QrCodeDTO init(String qrContent, String word, String format, int qrWidth, int qrHeight) {
        QrCodeDTO qrCodeDTO = new QrCodeDTO();
        qrCodeDTO.qrContent = qrContent;
        qrCodeDTO.word = word;
        qrCodeDTO.format = format;
        qrCodeDTO.width = qrWidth;
        qrCodeDTO.height = qrHeight;
        qrCodeDTO.wordHeight = DEFAULT_WORD_HEIGHT;

        Map<EncodeHintType, Object> encodeTypeMap = new HashMap<>();
        //编码
        encodeTypeMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //纠错等级，纠错等级越高存储信息越少
        encodeTypeMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        //边距
        encodeTypeMap.put(EncodeHintType.MARGIN, 2);
        qrCodeDTO.encodeTypeMap = encodeTypeMap;

        return qrCodeDTO;
    }

    public void setEncodeTypeMap(EncodeHintType encodeType, String type) {
        encodeTypeMap.put(encodeType, type);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getQrContent() {
        return qrContent;
    }

    public void setQrContent(String qrContent) {
        this.qrContent = qrContent;
    }

    public Map<EncodeHintType, Object> getEncodeTypeMap() {
        return encodeTypeMap;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getWordHeight() {
        return wordHeight;
    }

    public void setWordHeight(int wordHeight) {
        this.wordHeight = wordHeight;
    }

    public BufferedImage getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(BufferedImage logoImage) {
        this.logoImage = logoImage;
    }
}
