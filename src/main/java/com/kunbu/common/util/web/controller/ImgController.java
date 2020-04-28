package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.tool.file.FileDTO;
import com.kunbu.common.util.tool.file.FileUtil;
import com.kunbu.common.util.tool.img.utils.QrCodeUtil;
import com.kunbu.common.util.tool.img.utils.VerifyCodeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public void getQrCode(HttpServletRequest request, HttpServletResponse response, @RequestParam String text) {
        String qrContent = "https://xiao-2020-test.yunext.com/speedy/#/scanCode?id=f74c06a7aa804957ac34465b5fd81889";
        String logoPath = "demo/img/logo.png";

        byte[] qrBytes = QrCodeUtil.createQrCode(qrContent, text, logoPath);
        FileDTO fileDTO = FileDTO.of("qrCode.png", qrBytes);
        fileDTO.setInline(true);
        fileDTO.setSuccess(true);
        FileUtil.download(request, response, fileDTO);
    }

    @GetMapping("/sticker")
    public void getSticker(HttpServletRequest request, HttpServletResponse response, @RequestParam String text, @RequestParam String fileId) {

        FileDTO fileDTO = FileDTO.of("qrCode.png", null);
        fileDTO.setInline(true);
        fileDTO.setSuccess(true);
        FileUtil.download(request, response, fileDTO);
    }
}
