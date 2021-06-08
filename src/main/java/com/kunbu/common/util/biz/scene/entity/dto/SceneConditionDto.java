package com.kunbu.common.util.biz.scene.entity.dto;

import com.kunbu.common.util.biz.scene.entity.dto.condition.DeviceAttributeCondition;
import com.kunbu.common.util.biz.scene.entity.dto.condition.TimePointCondition;
import com.kunbu.common.util.biz.scene.entity.dto.condition.TimeRangeCondition;
import lombok.Data;

import java.io.Serializable;

@Data
public class SceneConditionDto implements Serializable {

    private Long id;

    private Long sceneId;

    /** 节点类型：1-trigger 2-condition */
    private Integer nodeType;

    private Integer sort;

    /** 1-时间点 2-时间段 3-设备属性 */
    private Integer conditionType;

    private DeviceAttributeCondition deviceAttributeCondition;

    private TimePointCondition timePointCondition;

    private TimeRangeCondition timeRangeCondition;

}
