package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.tool.file.FileUtil;
import com.kunbu.common.util.tool.img.qr.QrCodeUtil;
import com.kunbu.common.util.tool.img.verify.VerifyCodeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-23 17:09
 **/
@RestController
@RequestMapping("/img")
public class ImgController {

    @GetMapping("/verify")
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) {

        VerifyCodeUtil.getVerifyCode(request, response);
    }

    @GetMapping("/qr")
    public void getQrCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String qrContent = "https://xiao-2020-test.yunext.com/speedy/#/scanCode?id=f74c06a7aa804957ac34465b5fd81889";
        String qrText = "乘梯二维码";
        String logoPath = "C:\\Users\\mojun\\Desktop\\logo.png";

        byte[] qrBytes = QrCodeUtil.createQrCode(qrContent, qrText, logoPath);
        OutputStream out = response.getOutputStream();
        out.write(qrBytes);
        out.flush();
        FileUtil.download(request, response, qrBytes, "qrCode.png");
    }


}
