package com.kunbu.common.util;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-11-19 14:12
 **/
public class ResultMap {

    public static final Integer CODE_SUCCESS = 1;
    public static final Integer CODE_ERROR = 0;

    private String msg;
    private Integer code;
    private Object data;

    public static ResultMap success() {
        ResultMap result = new ResultMap();
        result.setCode(CODE_SUCCESS);
        return result;
    }

    public static ResultMap success(Object data) {
        ResultMap result = new ResultMap();
        result.setCode(CODE_SUCCESS);
        result.setData(data);
        return result;
    }

    public static ResultMap error(String msg) {
        ResultMap result = new ResultMap();
        result.setCode(CODE_ERROR);
        result.setMsg(msg);
        return result;
    }

    public static ResultMap error(String msg, int code) {
        ResultMap result = new ResultMap();
        result.setCode(code);
        result.setMsg(msg);
        return result;
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

}
