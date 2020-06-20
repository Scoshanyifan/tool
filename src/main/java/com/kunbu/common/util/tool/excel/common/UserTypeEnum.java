package com.kunbu.common.util.tool.excel.common;

/**
 * @author: KunBu
 * @time: 2020/6/20 14:11
 * @description:
 */
public enum UserTypeEnum {
    //
    owner("住户"),
    rent("租客"),
    family("家人"),
    cleaner("保洁"),


    ;

    private String value;

    UserTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserTypeEnum getByState(String state) {
        for (UserTypeEnum e : values()) {
            if (e.name().equals(state)) {
                return e;
            }
        }
        return null;
    }

    public static UserTypeEnum getByValue(String value) {
        for (UserTypeEnum e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
