package com.kunbu.common.util.web;

import com.kunbu.common.util.web.constant.ResultCode;

import java.io.Serializable;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-17 08:59
 **/
public class ServiceResult<T> implements Serializable {

    private boolean success;
    private T data;
    private ResultCode code;
    @Deprecated
    private String msg;

    public static ServiceResult success() {
        return success(null);
    }

    public static <T> ServiceResult success(T data) {
        ServiceResult result = new ServiceResult();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    @Deprecated
    public static ServiceResult fail() {
        ServiceResult result = new ServiceResult();
        result.setSuccess(false);
        return result;
    }

    @Deprecated
    public static ServiceResult fail(String msg) {
        ServiceResult result = new ServiceResult();
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }

    public static ServiceResult fail(ResultCode code) {
        ServiceResult result = new ServiceResult();
        result.setSuccess(false);
        result.setCode(code);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultCode getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
