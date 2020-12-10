package com.kunbu.common.util.biz.scene.entity.dto.action;

import com.kunbu.common.util.biz.scene.constant.SceneActionTypeEnum;
import lombok.Data;

/**
 * @author kunbu
 * @date 2020/12/7 10:24
 **/
@Data
public class DeviceSceneAction implements SceneActionBase {

    private Long sceneId;

    private Integer sort;

    private String attributeKey;
    /** 数据类型：1-布尔，2-整数，3-浮点，4-字符串，5-枚举 */
    private Integer dataType;
    /** 期望执行结果（开关-开，温度-30，设置模式-循环） */
    private Object expectValue;

    public DeviceSceneAction() {
    }

    public DeviceSceneAction(String attributeKey, Integer dataType, Object expectValue) {
        this.attributeKey = attributeKey;
        this.dataType = dataType;
        this.expectValue = expectValue;
    }

    @Override
    public SceneActionTypeEnum getActionTypeEnum() {
        return SceneActionTypeEnum.DEVICE_ATTR;
    }

    @Override
    public Long getSceneId() {
        return sceneId;
    }

    @Override
    public Integer getSort() {
        return sort;
    }
}
