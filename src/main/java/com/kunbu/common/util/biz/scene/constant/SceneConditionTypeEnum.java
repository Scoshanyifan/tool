package com.kunbu.common.util.biz.scene.constant;

public enum SceneConditionTypeEnum {

    TIME_POINT(1),
    TIME_RANGE(2),
    DEVICE_ATTR(3),
    WEATHER(4),
    ;

    private Integer conditionType;

    SceneConditionTypeEnum(Integer conditionType) {
        this.conditionType = conditionType;
    }

    public Integer getConditionType() {
        return conditionType;
    }

    public static SceneConditionTypeEnum of(Integer conditionType) {
        for (SceneConditionTypeEnum e : values()) {
            if (e.conditionType.equals(conditionType)) {
                return e;
            }
        }
        return null;
    }
}
