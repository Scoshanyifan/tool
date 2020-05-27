package com.kunbu.common.util.web;

import com.kunbu.common.util.web.constant.ApiCodeEnum;
import com.kunbu.common.util.web.constant.ResultCode;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-11-19 14:12
 **/
public class ApiResult {

    private String msg;
    private Integer code;
    private Object data;
    private boolean success;

    public static ApiResult success() {
        return success(null);
    }

    public static ApiResult success(Object data) {
        ApiResult result = new ApiResult();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    @Deprecated
    public static ApiResult fail(String msg) {
        ApiResult result = new ApiResult();
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }

    public static ApiResult fail(ResultCode code) {
        ApiResult result = new ApiResult();
        result.setSuccess(false);
        if (code != null) {
            result.setCode(code.getCode());
            result.setMsg(code.getMsg());
        }
        return result;
    }

    public static ApiResult fail(ResultCode apiCode, Object data) {
        ApiResult result = new ApiResult();
        result.setSuccess(false);
        if (apiCode != null) {
            result.setCode(apiCode.getCode());
            result.setMsg(apiCode.getMsg());
        }
        result.setData(data);
        return result;
    }

    public static ApiResult result(ServiceResult serviceResult) {
        if (serviceResult == null) {
            return fail(ApiCodeEnum.API_ERROR);
        }
        if (serviceResult.isSuccess()) {
            return success(serviceResult.getData());
        } else {
            return fail(serviceResult.getCode());
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
