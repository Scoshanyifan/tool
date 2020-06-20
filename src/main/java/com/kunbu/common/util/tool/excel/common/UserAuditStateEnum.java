package com.kunbu.common.util.tool.excel.common;

/**
 * @author: KunBu
 * @time: 2020/6/20 14:11
 * @description:
 */
public enum UserAuditStateEnum {
    //
    wait("待审核"),
    audit("已通过"),
    back("已驳回"),


    ;

    private String value;

    UserAuditStateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserAuditStateEnum getByState(String state) {
        for (UserAuditStateEnum e : values()) {
            if (e.name().equals(state)) {
                return e;
            }
        }
        return null;
    }

    public static UserAuditStateEnum getByValue(String value) {
        for (UserAuditStateEnum e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
