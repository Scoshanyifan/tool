package com.kunbu.common.util.biz.scene.constant;

public enum SceneActionTypeEnum {

    DELAY(1),
    DEVICE_ATTR(2),
    NOTIFY(3),


    WEATHER(4),
    SUN_RISE(5),
    SCENE(6),

    ;

    private Integer actionType;

    SceneActionTypeEnum(Integer actionType) {
        this.actionType = actionType;
    }

    public Integer getActionType() {
        return actionType;
    }

    public static SceneActionTypeEnum of(Integer actionType) {
        for (SceneActionTypeEnum e : values()) {
            if (e.actionType.equals(actionType)) {
                return e;
            }
        }
        return null;
    }
}
