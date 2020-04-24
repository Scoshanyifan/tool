package com.kunbu.common.util.tool.img.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-22 16:01
 **/
public class QrCodeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeUtil.class);

    public static byte[] createQrCode2Byte(QrCodeDTO qrCode) {
        try {
            BufferedImage bufferedImage = drawQRCode(qrCode);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, qrCode.getFormat(), bos);
            byte[] qrImgBytes = bos.toByteArray();
            bos.close();
            return qrImgBytes;
        } catch (IOException e) {
            LOGGER.error(">>> createQrImg2Byte error", e);
        }
        return null;
    }

    /**
     * @param qrCode 配置
     * @description 生成带logo的二维码图片 二维码下面带文字
     */
    private static BufferedImage drawQRCode(QrCodeDTO qrCode) {
        // 绘制二维码
        BitMatrix qrBitMatrix = createQrCodeBitMatrix(qrCode.getQrContent(), qrCode.getWidth(), qrCode.getHeight(), qrCode.getEncodeTypeMap());
        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(qrBitMatrix);
        // 绘制logo
        BufferedImage logoImage = qrCode.getLogoImage();
        if (logoImage != null) {
            // 属性抗锯齿
            Graphics2D g = qrCodeImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
            g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            // 等比缩放：（width * 2 / 10 height * 2 / 10）不需缩放直接使用图片宽高，logo绘制在中心点位置
            g.drawImage(logoImage, qrCode.getWidth() * 2 / 5, qrCode.getHeight() * 2 / 5, logoImage.getWidth(), logoImage.getHeight(), null);
            g.dispose();
            logoImage.flush();
        }
        // 添加文字在二维码下方
        String word = qrCode.getWord();
        if (word != null) {
            //创建一个带透明色的BufferedImage对象
            BufferedImage outImage = new BufferedImage(qrCode.getWidth(), qrCode.getHeight() + qrCode.getWordHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D outG = outImage.createGraphics();
            outG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            outG.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
            outG.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            // 二维码绘制到新的面板 TODO
            outG.drawImage(qrCodeImage, 0, 0, qrCodeImage.getWidth(), qrCodeImage.getHeight(), null);
            // 字体颜色，大小
            outG.setColor(new Color(183, 183, 183));
            outG.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            //文字长度
            int strWidth = outG.getFontMetrics().stringWidth(word);
            //总长度减去文字长度的一半  （居中显示）
            int wordStartX = (qrCode.getWidth() - strWidth) / 2;
            //height + (outImage.getHeight() - height) / 2 + 12
            int wordStartY = qrCode.getWidth() + 10;
            // 画文字
            outG.drawString(word, wordStartX, wordStartY);
            outG.dispose();
            outImage.flush();
            qrCodeImage = outImage;
        }
        qrCodeImage.flush();
        return qrCodeImage;
    }



    private static BitMatrix createQrCodeBitMatrix(String content, int qrWidth, int qrHeight, Map<EncodeHintType, Object> hints) {
        try {
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
            return bitMatrix;
        } catch (WriterException e) {
            LOGGER.error(">>> createBitMatrix error", e);
        }
        return null;
    }


}
