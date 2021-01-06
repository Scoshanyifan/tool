package com.kunbu.common.util.biz.scene.constant;

public enum SceneTimeTypeEnum {

    ONCE(1),
    WORK_DAY(2),
    WEEKEND(3),
    EVERYDAY(4),
    DAY_OF_WEEK(5),

    ;

    private Integer type;

    public Integer getType() {
        return type;
    }

    SceneTimeTypeEnum(Integer type) {
        this.type = type;
    }

    public static SceneTimeTypeEnum of(Integer timeType) {
        for (SceneTimeTypeEnum e : values()) {
            if (e.type.equals(timeType)) {
                return e;
            }
        }
        return null;
    }
}
