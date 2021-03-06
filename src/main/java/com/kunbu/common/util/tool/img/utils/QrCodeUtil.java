package com.kunbu.common.util.tool.img.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.kunbu.common.util.tool.img.ImgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-22 16:01
 **/
public class QrCodeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeUtil.class);

    private static final String DEFAULT_CHARSET             = "utf-8";
    private static final int DEFAULT_QR_WIDTH               = 400;
    private static final int DEFAULT_QR_HEIGHT              = 400;

    /**
     * 仅生成二维码
     *
     * @param qrContent 二维码内容
     * @return png
     **/
    public static byte[] createQrCode(String qrContent) {
        return createQrCode(qrContent, null, null);
    }

    /**
     * 生成带底部文字，logo的二维码
     *
     * @param qrContent 二维码内容
     * @param qrText 二维码底部文字
     * @param logoPath 二维码中间的logo
     * @return png
     **/
    public static byte[] createQrCode(String qrContent, String qrText, String logoPath) {
        BufferedImage logoBufImg = null;
        if (logoPath != null) {
            logoBufImg = ImgUtil.getBufImg(logoPath);
        }
        return createQrCode(qrContent, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT, logoBufImg, qrText, ImgUtil.IMAGE_FORMAT_PNG);
    }

    /**
     * 生成自定义二维码
     *
     * @param qrContent 二维码内容
     * @param qrWidth 二维码宽
     * @param qrHeight 二维码高
     * @param logoBufImg 二维码中间的logo
     * @param qrText 二维码底部文字
     * @return
     **/
    public static byte[] createQrCode(String qrContent, Integer qrWidth, Integer qrHeight, BufferedImage logoBufImg, String qrText, String imageFormat) {
        BufferedImage qrBufImg = createQrCodeWithLogoAndText(qrContent, qrWidth, qrHeight, logoBufImg, qrText);
        return ImgUtil.convertBufImg2Bytes(qrBufImg, imageFormat);
    }

    private static BufferedImage createQrCodeWithLogoAndText(String qrContent, int qrWidth, int qrHeight, BufferedImage logoBufImg, String qrText) {
        // 1. 生成二维码
        BufferedImage qrBufImg;
        try {
            // 二维码配置
            Map<EncodeHintType, Object> encodeTypeMap = new HashMap<>(8);
            // 1.编码
            encodeTypeMap.put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARSET);
            // 2.纠错等级，纠错等级越高存储信息越少
            encodeTypeMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // 3.边距
            encodeTypeMap.put(EncodeHintType.MARGIN, 2);
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, qrWidth, qrHeight, encodeTypeMap);
            qrBufImg = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 2. 插入logo TODO 生成的二维码图层是黑白ARGB，需要改成全彩
        if (logoBufImg != null) {
            qrBufImg = ImgUtil.newImg(qrBufImg, qrWidth, qrWidth, null);
            logoBufImg = ImgUtil.scaleImg(logoBufImg, 0.9);
            int logoX = (qrBufImg.getWidth() - logoBufImg.getWidth()) / 2;
            int logoY = (qrBufImg.getHeight() - logoBufImg.getHeight()) / 2;
            ImgUtil.joinImg(qrBufImg, logoBufImg, logoX, logoY);
        }
        // 3. 扩展图层, 插入文字，高度为二维码的1.1
        if (qrText != null) {
            qrBufImg = ImgUtil.newImg(qrBufImg, qrWidth, (int) (qrHeight * 1.1), Color.WHITE);
            ImgUtil.addText(qrBufImg, qrText, null, null, true, qrBufImg.getHeight() / 15);
        }
        return qrBufImg;
    }

    public static void main(String[] args) {
        FileOutputStream fos = null;
        try {
            String qrContent = "https://xiao-2020-test.yunext.com/speedy/#/scanCode?id=f74c06a7aa804957ac34465b5fd81889";
            String qrText = "乘梯二维码A11";
            String logoPath = "common/img/logo.png";

            byte[] bytes = createQrCode(qrContent, qrText, logoPath);

            File file = new File("common/img/qrCode.png");
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            LOGGER.error(">>> error", e);
        }  finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOGGER.error(">>> error", e);
                }
            }
        }
    }
}
