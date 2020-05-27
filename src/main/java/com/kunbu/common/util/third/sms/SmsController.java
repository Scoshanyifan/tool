package com.kunbu.common.util.third.sms;

import com.kunbu.common.util.web.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-05-18 16:55
 **/
@RestController
@RequestMapping("/sms")
public class SmsController {

    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private SmsService smsService;

    @GetMapping("/code")
    public ApiResult getSmsVerifyCode(@RequestParam String phone) {

        return ApiResult.result(smsService.sendSmsCode(phone));
    }

    @GetMapping("/check")
    public ApiResult checkSmsVerifyCode(@RequestParam String phone, @RequestParam String code) {

        return ApiResult.result(smsService.checkSmsCode(phone, code));
    }
}
