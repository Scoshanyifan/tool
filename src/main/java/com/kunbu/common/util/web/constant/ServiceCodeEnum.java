package com.kunbu.common.util.web.constant;

/**
 * @author: KunBu
 * @time: 2020/4/17 9:03
 * @description:
 */
public enum ServiceCodeEnum implements ResultCode {
    //
    SERVICE_ERROR(200, "服务异常"),

    SMS_PHONE_EXIST(201, "验证码已发送，请稍后再试"),
    SMS_CODE_ERROR(201, "验证码错误，请重试"),
    SMS_SEND_FAILURE(202, "验证码发送失败，请稍后再试"),

    ;

    private Integer code;
    private String msg;

    ServiceCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
