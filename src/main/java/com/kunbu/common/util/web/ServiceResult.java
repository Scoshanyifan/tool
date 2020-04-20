package com.kunbu.common.util.web;

import com.kunbu.common.util.web.code.ResultCode;

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
}
