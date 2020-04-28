package com.kunbu.common.util.tool.img;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-24 11:10
 **/
public class ImgUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImgUtil.class);

    private static final String FONT_MSYH               = "MSYH.TTF";
    private static final String FONT_DEFAULT            = "微软雅黑";

    public static final String IMAGE_FORMAT_PNG         = "png";
    public static final String IMAGE_FORMAT_JPG         = "jpg";
    public static final String PATH_TYPE_HTTP           = "http";

    /**
     * 通过路径获取图层
     *
     * @param path
     * @return
     **/
    public static BufferedImage getBufImg(String path) {
        try {
            if (path.startsWith(PATH_TYPE_HTTP)) {
                return ImageIO.read(new URL(path));
            } else {
                return ImageIO.read(new File(path));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过流获取图层
     *
     * @param stream
     * @return
     **/
    public static BufferedImage getBufImg(InputStream stream) {
        try {
            return ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将图层转换成字节
     *
     * @param bufImg
     * @param imageFormat
     * @return
     **/
    public static byte[] convertBufImg2Bytes(BufferedImage bufImg, String imageFormat) {
        if (bufImg == null) {
            return null;
        }
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ImageIO.write(bufImg, imageFormat == null ? IMAGE_FORMAT_PNG : imageFormat, bos);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    LOGGER.error(">>> close error", e);
                }
            }
        }
    }

    public static byte[] changeDpi(BufferedImage bufImg, String imageType, int dpi) throws Exception {
        if (IMAGE_FORMAT_PNG.equals(imageType)) {
            return DpiUtil.processPNG(bufImg, dpi);
        } else {
            return DpiUtil.processJPG(bufImg, dpi);
        }
    }

    /**
     * 拼接图片
     *
     * @param outBufImg
     * @param inBufImg
     * @param inImgX 左边距，起点是左上角
     * @param inImgY 上边距
     * @return
     **/
    public static void joinImg(BufferedImage outBufImg, BufferedImage inBufImg, int inImgX, int inImgY) {
        Graphics2D outG = null;
        try {
            outG = outBufImg.createGraphics();
            setRender(outG);
            outG.drawImage(inBufImg, inImgX, inImgY, inBufImg.getWidth(), inBufImg.getHeight(), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (outG != null) {
                outG.dispose();
            }
        }
    }

    /**
     * 绘制新图层，旧图层以外为透明
     *
     * @param oldBufImg
     * @param targetWidth
     * @param targetHeight
     * @return
     **/
    public static BufferedImage newImg(BufferedImage oldBufImg, int targetWidth, int targetHeight) {
        Graphics2D newG = null;
        try {
            BufferedImage qrBufImgNew = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
            newG = qrBufImgNew.createGraphics();
            newG.drawImage(oldBufImg, 0, 0, oldBufImg.getWidth(), oldBufImg.getHeight(), null);
            return qrBufImgNew;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (newG != null) {
                newG.dispose();
            }
        }
    }

    /**
     * 缩放图片
     *
     * @param bufImg
     * @param targetWidth
     * @param targetHeight
     **/
    public static BufferedImage scaleImg(BufferedImage bufImg, int targetWidth, int targetHeight) {
        Graphics scaleG = null;
        try {
            BufferedImage imageNew = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            scaleG = imageNew.getGraphics();
            Image tempIng = bufImg.getScaledInstance(targetWidth, targetHeight, BufferedImage.SCALE_DEFAULT);
            scaleG.drawImage(tempIng, 0, 0, null);
            return imageNew;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (scaleG != null) {
                scaleG.dispose();
            }
        }
    }

    /**
     * 填充文字，居中
     *
     * @param bufImg
     * @param text
     * @param font 默认：微软雅黑
     * @param color 默认：黑色
     * @param ifBottom 文字位于顶部或底部，默认：底部
     * @param textPadding 文字距离顶部或底部距离，默认：1/15的图片宽度
     */
    public static void addText(BufferedImage bufImg, String text, String font, Color color, Boolean ifBottom, Integer textPadding) {
        Graphics2D graphics2D = null;
        try {
            graphics2D = bufImg.createGraphics();
            setRender(graphics2D);
            if (font == null) {
                font = FONT_DEFAULT;
            }
            if (color == null) {
                color = Color.BLACK;
            }
            if (ifBottom == null) {
                ifBottom = true;
            }
            /**
             * 计算字体大小
             **/
            int imgWidth = bufImg.getWidth();
            if (textPadding == null) {
                textPadding = imgWidth / 15;
            }
            // 计算文字数量
            int textLength = calTextLength(text);
            // 计算字体宽度，最大取1/15的图片宽度
            int fontWidth = imgWidth / 15;
            // 文字总宽度取图片宽度的3/5计算
            while (fontWidth * textLength > imgWidth * 0.8) {
                fontWidth -= 1;
            }
            // 计算字体大小
            int fontSize = calTextSize(fontWidth);
            // 设置字体
            graphics2D.setFont(new Font(font, Font.PLAIN, fontSize));
            // 设置颜色
            graphics2D.setColor(color);
            /**
             * 计算文字位置，起点是左上角，但是从文字左下角计算
             **/
            int textWidth = textLength * fontSize;
            int textWidthSys = graphics2D.getFontMetrics().stringWidth(text);
            LOGGER.info(">>> textLength:{}, fontWidth:{}, fontSize:{}, textWidth:{}, textWidthSys:{}", textLength, fontWidth, fontSize, textWidth, textWidthSys);
            // 计算X轴，居中
            int textStartX = (imgWidth - textWidth) / 2;
            // 计算Y轴
            int textStartY;
            if (ifBottom) {
                textStartY = bufImg.getHeight() - textPadding;
            } else {
                textStartY = textPadding + fontWidth;
            }
            // 绘制文字
            graphics2D.drawString(text, textStartX, textStartY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (graphics2D != null) {
                graphics2D.dispose();
            }
        }
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
    private static int calTextLength(String text) {
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

    /**
     * 根据像素px，动态计算字体大小，从小四开始，初号结束
     *
     * @param fontWidth 字体宽度
     * @return
     **/
    private static int calTextSize(int fontWidth) {
        if (fontWidth <= 16) {
            // 小四
            return 12;
        } else if (16 < fontWidth && fontWidth <= 18) {
            // 四号
            return 14;
        } else if (18 < fontWidth && fontWidth <= 20) {
            // 小三
            return 15;
        } else if (fontWidth == 21) {
            // 三号
            return 16;
        } else if (21 < fontWidth && fontWidth <= 24) {
            // 小二
            return 18;
        } else if (24 < fontWidth && fontWidth <= 29) {
            // 二号
            return 22;
        } else if (29 < fontWidth && fontWidth <= 32) {
            // 小一
            return 24;
        } else if (32 < fontWidth && fontWidth <= 34) {
            // 一号
            return 26;
        } else if (34 < fontWidth && fontWidth <= 48) {
            // 小初
            return 36;
        } else {
            // 初号
            return 42;
        }
    }

    public static void main(String[] args) {
        FileOutputStream fos = null;
        try {
            BufferedImage outImg = ImageIO.read(new File("demo/img/qr.png"));
            BufferedImage qrImgOld = ImageIO.read(new File("demo/img/cat.png"));
            BufferedImage qrImg = ImgUtil.scaleImg(qrImgOld, (int) (qrImgOld.getWidth() * 0.9), (int) (qrImgOld.getHeight() * 0.9));

            // 二维码居中
            int startX = (outImg.getWidth() - qrImg.getWidth()) / 2;
            // 起始Y轴，距离顶部1/4主图高度
            int startY = outImg.getHeight() / 4;

            ImgUtil.joinImg(outImg, qrImg, startX, startY);
            ImgUtil.addText(outImg, "这是1张二维码图片-ABC", null, null, null, null);

            boolean changeDpi = false;
            byte[] bytes;
            if (changeDpi) {
                bytes = ImgUtil.changeDpi(outImg, ImgUtil.IMAGE_FORMAT_PNG, 300);
            } else {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(outImg, "png", bos);
                bytes = bos.toByteArray();
            }

            File file = new File("demo/img/demo.png");
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
