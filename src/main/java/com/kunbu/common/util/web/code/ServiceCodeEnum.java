package com.kunbu.common.util.web.code;

/**
 * @author: KunBu
 * @time: 2020/4/17 9:03
 * @description:
 */
public enum ServiceCodeEnum implements ResultCode {
    //
    SERVICE_ERROR(200, "服务异常"),

    ;

    private Integer code;
    private String msg;

    ServiceCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return null;
    }

    @Override
    public String getMsg() {
        return null;
    }
}
