package com.kunbu.common.util.third.sms;

import com.alibaba.fastjson.JSONObject;
import com.kunbu.common.util.basic.DateFormatUtil;
import com.kunbu.common.util.basic.MD5Util;
import com.kunbu.common.util.tool.net.NetConstant;
import com.kunbu.common.util.tool.net.NetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.Map;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-05-18 16:02
 **/
public class SmsUtil {

    private static Logger log = LogManager.getLogger(SmsUtil.class);

//    private static final String SMS_APPID = "EUCP-EMY-SMS1-2XW5H";
//    private static final String SMS_SECRET = "2020AEC0E3350FDA";

    private static final String SMS_SEND_URL = "http://shmtn.b2m.cn/simpleinter/sendSMS" +
            "?appId=APPID&timestamp=TIMESTAMP&sign=SIGN&mobiles=MOBILES&content=CONTENT";

    public static Boolean sendSmsCode(String contentPrefix, String mobile, String smsCode, String appid, String secret) {
        try {
            String context = null;
            if (contentPrefix != null) {
                context = contentPrefix;
            }
            context = context + "短信验证码:" + smsCode;

            String timestamp = DateFormatUtil.format(new Date(), "yyyyMMddHHmmss");
            String sign = MD5Util.getMD5(appid + secret + timestamp);
            String url = SMS_SEND_URL
                    .replace("APPID", appid)
                    .replace("TIMESTAMP", timestamp)
                    .replace("SIGN", sign)
                    .replace("MOBILES", mobile)
                    .replace("CONTENT", context);

            Map<String, Object> result = NetUtil.doGet(url);
            log.info("sms send message result:" + result);

            JSONObject smsData = (JSONObject) result.get(NetConstant.NET_RESULT_DATA);
            boolean resultBeta = smsData.get("code") != null && result.get("code").equals("SUCCESS");
            return resultBeta;
        } catch (Exception e) {
            log.error("sms error:", e);
            return false;
        }
    }

}
