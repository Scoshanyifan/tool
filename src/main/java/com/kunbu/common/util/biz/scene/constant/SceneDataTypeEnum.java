package com.kunbu.common.util.biz.scene.constant;

public enum SceneDataTypeEnum {

    BOOLEAN_TYPE(1),
    INT_TYPE(2),
    FLOAT_TYPE(3),
    STRING_TYPE(4),
    ENUM_TYPE(5),


    ;

    private Integer dataType;

    SceneDataTypeEnum(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getDataType() {
        return dataType;
    }

    public static SceneDataTypeEnum of(Integer dataType) {
        for (SceneDataTypeEnum e : values()) {
            if (e.dataType.equals(dataType)) {
                return e;
            }
        }
        return null;
    }
}
