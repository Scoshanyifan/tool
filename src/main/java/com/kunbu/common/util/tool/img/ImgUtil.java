package com.kunbu.common.util.tool.img;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-24 11:10
 **/
public class ImgUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImgUtil.class);

    private static final String FONT_DEFAULT    = "微软雅黑";
    private static final String IMAGE_PNG       = "png";



    public static byte[] changeDpi(BufferedImage image, String imageType, int dpi) throws Exception {
        if (IMAGE_PNG.equals(imageType)) {
            return DpiUtil.processPNG(image, dpi);
        } else {
            return DpiUtil.processJPG(image, dpi);
        }
    }

    /**
     * 拼接图片
     *
     * @param outG
     * @param inImg
     * @param inImgX 左边距，起点是左上角
     * @param inImgY 上边距
     * @return
     **/
    public static void joinImg(Graphics2D outG, BufferedImage inImg, int inImgX, int inImgY) {
        // 绘制图片
        outG.drawImage(inImg, inImgX, inImgY, inImg.getWidth(), inImg.getHeight(), null);
    }

    /**
     * 缩放图片
     *
     * @param image
     * @param targetWidth
     * @param targetHeight
     * @param scale
     **/
    public static BufferedImage scaleImg(BufferedImage image, int targetWidth, int targetHeight, Integer scale) {
        BufferedImage imageNew = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Image tempIng = image.getScaledInstance(targetWidth, targetHeight, scale == null ? BufferedImage.SCALE_DEFAULT : scale);
        imageNew.getGraphics().drawImage(tempIng, 0, 0, null);
        return imageNew;
    }

    /**
     * 填充文字
     *
     * @param graphics2D
     * @param text
     * @param imgWidth 图片宽度，用于计算字体大小
     * @param imgX
     * @param imgY
     * @param font
     * @param color
     */
    private static void addText(Graphics2D graphics2D, String text, int imgWidth, int imgX, int imgY, int imgBottom, String font, Color color) {
        if (font == null) {
            font = FONT_DEFAULT;
        }
        if (color == null) {
            color = Color.BLACK;
        }
        // 计算文字数量
        int textLength = calTextActualLength(text);
        // 计算字体大小，取图片长度的9/10
        int fontSize = (imgWidth - imgWidth / 10) / textLength;
        // 如果字体大于底部空间，则缩放
        if (fontSize > imgBottom) {
            fontSize = imgBottom + 2;
        }
        // 设置字体
        graphics2D.setFont(new Font(font, Font.PLAIN, fontSize));
        // 设置颜色
        graphics2D.setColor(color);

        // 计算文字位置，起点是左上角，但是从文字左下角计算
        int textWidth = graphics2D.getFontMetrics().stringWidth(text);
        int textStartX = imgX + (imgWidth - textWidth) / 2;
        int textStartY = (int) (imgY + imgWidth * 1.1);
        // 绘制文字
        graphics2D.drawString(text, textStartX, textStartY);
    }

    /**
     * 设置抗锯齿
     *
     * @param graphics2D
     *
     **/
    private static void setRender(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        graphics2D.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
    }

    /**
     * 动态计算文本长度，小写英文算0.6个中文，大写算0.8个中文
     * @param text
     * @return
     */
    private static int calTextActualLength(String text) {
        char[] crs = text.toCharArray();
        double count = 0;
        for (char c : crs) {
            if (65 <= c && c <= 90) {
                count += 0.8;
            } else if (97 <=c && c <= 122) {
                count += 0.6;
            } else {
                count++;
            }
        }
        return (int) count;
    }

    public static void main(String[] args) {

        Graphics2D outG = null;
        try {
            BufferedImage outImg = ImageIO.read(new File("C:\\Users\\mojun\\Desktop\\img.jpg"));
            BufferedImage qrImgOld = ImageIO.read(new File("C:\\Users\\mojun\\Desktop\\qr.png"));
            BufferedImage qrImg = ImgUtil.scaleImg(qrImgOld, (int) (qrImgOld.getWidth() * 0.9), (int) (qrImgOld.getHeight() * 0.9), null);

            outG = outImg.createGraphics();
            // 二维码居中
            int startX = (outImg.getWidth() - qrImg.getWidth()) / 2;
            // 底部空出0.2的二维码高度
            int startY = (int) (outImg.getHeight() - qrImg.getHeight() * 1.2);

            ImgUtil.setRender(outG);
            ImgUtil.joinImg(outG, qrImg, startX, startY);
            ImgUtil.addText(outG, "这是1张二维码图片-ABC", qrImg.getWidth(), startX, startY, qrImg.getHeight() / 20, null, null);

            boolean changeDpi = false;
            byte[] bytes;
            if (changeDpi) {
                bytes = ImgUtil.changeDpi(outImg, ImgUtil.IMAGE_PNG, 300);
            } else {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(outImg, "png", bos);
                bytes = bos.toByteArray();
            }

            File file = new File("C:\\Users\\mojun\\Desktop\\test.jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            LOGGER.error(">>> error", e);
        } finally {
            if (outG != null) {
                outG.dispose();
            }
        }
    }

}
