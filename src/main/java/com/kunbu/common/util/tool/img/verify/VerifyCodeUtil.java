package com.kunbu.common.util.tool.img.verify;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-23 10:24
 **/
public class VerifyCodeUtil {

    /** 设置图片宽度和高度 */
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 45;
    /** 干扰线条数 */
    private static final int DEFAULT_LINE = 10;
    private static final int DEFAULT_LENGTH = 4;

    public static void getVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        String VerifyCode = "";
        Random r = new Random();
        // 验证码图片
        BufferedImage codeImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = codeImage.getGraphics();
        g.setFont(new Font("宋体", Font.BOLD, 30));
        for (int i = 0; i < DEFAULT_LENGTH; i++) {
            int number = r.nextInt(10);
            // 随机颜色，RGB模式
            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);
            // 10~40范围内的一个整数，作为y坐标
            g.drawString(Integer.toString(number), 5 + i * DEFAULT_WIDTH / 4, 10 + r.nextInt(30));
            VerifyCode += number;
        }
        request.getSession().setAttribute("VERIFY_CODE", VerifyCode);

        for (int i = 0; i < DEFAULT_LINE; i++) {
            // 设置干扰线颜色
            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);
            g.drawLine(r.nextInt(DEFAULT_WIDTH), r.nextInt(DEFAULT_HEIGHT), r.nextInt(DEFAULT_WIDTH), r.nextInt(DEFAULT_HEIGHT));
        }
        // 清空缓冲
        g.dispose();
        try {
            ImageIO.write(codeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
