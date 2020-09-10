package com.kunbu.common.util.third.sms;

import com.kunbu.common.util.basic.RandomUtil;
import com.kunbu.common.util.tool.sql.redis.RedisManager;
import com.kunbu.common.util.web.ServiceResult;
import com.kunbu.common.util.web.constant.ServiceCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-05-19 11:10
 *
 * 验证码每60s可以发送一次，有效期10分钟，最多10个有效
 *
 **/
@Service
public class SmsService {

    private static Logger logger = LoggerFactory.getLogger(SmsService.class);

    private static final String CACHE_SMS_PHONE_KEY = "sms:phone:";
    private static final String CACHE_SMS_CODE_KEY = "sms:code:";
    private static final String CACHE_KEY_SPLITTER = ":";

    private static final long SMS_CODE_EXPIRE_TIME = 60 * 5L;
    private static final long SMS_PHONE_EXPIRE_TIME = 60;

    private static final String SMS_CONTENT_PREFIX = "【测试】";

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private SmsConfig smsConfig;

    public ServiceResult sendSmsCode(String phone) {

        boolean exists = redisManager.exists(CACHE_SMS_PHONE_KEY + phone);
        boolean existKey = redisManager.existKey(CACHE_SMS_PHONE_KEY + phone);
        logger.info(">>> exists:{}, existKey:{}", exists, existKey);
        if (existKey) {
            return ServiceResult.fail(ServiceCodeEnum.SMS_PHONE_EXIST);
        }
        // 删除该phone下老的code
//        Set<String> keys = redisManager.getKeys(CACHE_SMS_CODE_KEY + phone + ":*");
//        if (CollectionUtils.isNotEmpty(keys)) {
//            for (String key : keys) {
//                redisManager.delKey(key);
//            }
//        }

        String smsCode = RandomUtil.randomNumberStr(4);
        Boolean sendRes = SmsUtil.sendSmsCode(SMS_CONTENT_PREFIX, phone, smsCode, smsConfig.getAppId(), smsConfig.getSecretKey());
        if (sendRes) {
            redisManager.set(CACHE_SMS_PHONE_KEY + phone, "obj", SMS_PHONE_EXPIRE_TIME);
            redisManager.set(CACHE_SMS_CODE_KEY + phone + CACHE_KEY_SPLITTER + smsCode, "obj", SMS_CODE_EXPIRE_TIME);
            return ServiceResult.success();
        } else {
            return ServiceResult.fail(ServiceCodeEnum.SMS_SEND_FAILURE);
        }
    }

    public ServiceResult checkSmsCode(String phone, String code) {
        boolean delRes = redisManager.delKey(CACHE_SMS_CODE_KEY + phone + CACHE_KEY_SPLITTER + code);
        if (delRes) {
            return ServiceResult.success();
        } else {
            return ServiceResult.fail(ServiceCodeEnum.SMS_CODE_ERROR);
        }
    }

}
