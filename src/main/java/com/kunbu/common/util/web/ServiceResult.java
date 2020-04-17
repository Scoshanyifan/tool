package com.kunbu.common.util.web;

import com.kunbu.common.util.web.code.ResultCode;

import java.io.Serializable;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-17 08:59
 **/
public class ServiceResult implements Serializable {

    private boolean success;
    private Object data;
    private ResultCode code;

    public static ServiceResult success() {
        return success(null);
    }

    public static ServiceResult success(Object data) {
        ServiceResult result = new ServiceResult();
        result.setSuccess(true);
        result.setData(data);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResultCode getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }
}
