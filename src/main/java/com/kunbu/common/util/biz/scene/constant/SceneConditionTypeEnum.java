package com.kunbu.common.util.biz.scene.constant;

public enum SceneConditionTypeEnum {

    CRON(1),
    DEVICE_ATTR(2),

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
