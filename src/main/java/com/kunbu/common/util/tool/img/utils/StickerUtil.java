package com.kunbu.common.util.tool.img.utils;

import com.kunbu.common.util.tool.img.ImgUtil;
import org.springframework.beans.factory.annotation.Value;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-28 17:20
 **/
public class StickerUtil {

    private static String TEMPLATE_PATH;
    private static final int CONTENT_X = 166;
    private static final int CONTENT_Y = 600;
    private static final int VALID_WIDTH = 630;
    private static final int VALID_HEIGHT = 700;


    @Value("${img.sticker}")
    public void setTemplatePath(String templatePath) {
        TEMPLATE_PATH = templatePath;
    }

    /**
     * 生成ycb桌贴
     *
     * X：166 Y：600
     *
     * @param qrContent
     * @param text
     * @return
     **/
    public static byte[] createSticker(byte[] qrContent, String text) {

        BufferedImage templateBufImg = ImgUtil.getBufImg(TEMPLATE_PATH);
        BufferedImage contentBufImg = ImgUtil.getBufImg(new ByteArrayInputStream(qrContent));
        contentBufImg = ImgUtil.scaleImg(contentBufImg, VALID_WIDTH, VALID_WIDTH);

        ImgUtil.joinImg(templateBufImg, contentBufImg, CONTENT_X, CONTENT_Y);
//        ImgUtil.addText(templateBufImg);

        return null;
    }

}
