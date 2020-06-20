package com.kunbu.common.util.tool.excel.common;

/**
 * @author: KunBu
 * @time: 2020/6/20 14:11
 * @description:
 */
public enum SexEnum {
    //
    man("男"),
    woman("女"),


    ;

    private String value;

    SexEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SexEnum getByState(String state) {
        for (SexEnum e : values()) {
            if (e.name().equals(state)) {
                return e;
            }
        }
        return null;
    }

    public static SexEnum getByValue(String value) {
        for (SexEnum e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
