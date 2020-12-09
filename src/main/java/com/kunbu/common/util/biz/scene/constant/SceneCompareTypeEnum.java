package com.kunbu.common.util.biz.scene.constant;

public enum SceneCompareTypeEnum {

    EQUALS(1),
    LESS_THAN(2),
    GRATER_THAN(3),

    NOT_EQUALS(4),
    LESS_THAN_EQUALS(5),
    GRATER_THAN_EQUALS(6),


    ;

    private Integer compareType;

    SceneCompareTypeEnum(Integer compareType) {
        this.compareType = compareType;
    }

    public Integer getCompareType() {
        return compareType;
    }

    public static SceneCompareTypeEnum of(Integer compareType) {
        for (SceneCompareTypeEnum e : values()) {
            if (e.compareType.equals(compareType)) {
                return e;
            }
        }
        return null;
    }
}
