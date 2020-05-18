package com.kunbu.common.util.third.sms;

import com.kunbu.common.util.basic.RandomUtil;
import com.kunbu.common.util.tool.sql.redis.RedisManager;
import com.kunbu.common.util.web.ApiResult;
import com.kunbu.common.util.web.constant.ApiCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-05-18 16:55
 **/
@RestController
@RequestMapping("/sms")
public class SmsController {

    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    private static final String CACHE_SMS_PHONE_KEY = "sms:phone:";

    private static final long SMS_CODE_EXPIRE_TIME = 60 * 5L;

    private static final String SMS_CONTENT_PREFIX = "kunbu";

    @Autowired
    private RedisManager redisManager;


    @GetMapping("/code")
    public ApiResult getSmsVerifyCode(@RequestParam String phone) {

        boolean exists = redisManager.exists(phone);
        boolean existKey = redisManager.existKey(phone);
        logger.info(">>> exists:{}, existKey:{}", exists, existKey);
        if (existKey) {
            return ApiResult.fail(ApiCodeEnum.SMS_PHONE_EXIST);
        }

        String smsCode = RandomUtil.randomNumberStr(4);
        Boolean sendRes = SmsUtil.sendSmsCode(SMS_CONTENT_PREFIX, phone, smsCode);
        if (sendRes) {
            redisManager.set(CACHE_SMS_PHONE_KEY + phone, smsCode, SMS_CODE_EXPIRE_TIME);
            return ApiResult.success();
        } else {
            return ApiResult.fail(ApiCodeEnum.SMS_SEND_FAILURE);
        }
    }
}
