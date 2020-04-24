package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.tool.file.FileUtil;
import com.kunbu.common.util.tool.img.qr.QrCodeDTO;
import com.kunbu.common.util.tool.img.qr.QrCodeUtil;
import com.kunbu.common.util.tool.img.verify.VerifyCodeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public void getQrCode(HttpServletRequest request, HttpServletResponse response) {
        QrCodeDTO qrCodeDTO = QrCodeDTO.init(
                "https://xiao-2020-test.yunext.com/speedy/#/scanCode?id=f74c06a7aa804957ac34465b5fd81889",
                "乘梯二维码");

        QrCodeUtil.createQrCode2Byte(qrCodeDTO);
        FileUtil.setInlineOrAttachment(response, "乘梯二维码", true);
    }


}
