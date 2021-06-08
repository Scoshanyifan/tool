package com.kunbu.common.util.biz.scene.entity.bean;

import com.alibaba.fastjson.JSONObject;
import com.kunbu.common.util.biz.scene.constant.SceneConditionTypeEnum;
import com.kunbu.common.util.biz.scene.entity.dto.condition.DeviceAttributeCondition;
import com.kunbu.common.util.biz.scene.entity.dto.condition.SceneConditionBase;
import com.kunbu.common.util.biz.scene.entity.dto.condition.TimePointCondition;
import com.kunbu.common.util.biz.scene.entity.dto.condition.TimeRangeCondition;
import lombok.Data;

import java.util.Date;

/**
 * @author kunbu
 * @date 2020/12/2 11:47
 **/
@Data
public class SceneCondition {

    private Long id;

    private Long sceneId;

    /** 节点类型：1-trigger 2-condition */
    private Integer nodeType;

    private Integer sort;

    private String userUuid;

    /** 1-时间点 2-时间段 3-设备属性 */
    private Integer conditionType;

    /** 具体条件，区分时间和设备属性 */
    private String conditionJson;

    private Date createTime;

    private Date updateTime;

    public @interface Update {}

    public SceneConditionBase getCondition() {
        SceneConditionTypeEnum sceneConditionTypeEnum = SceneConditionTypeEnum.of(conditionType);
        switch (sceneConditionTypeEnum) {
            case DEVICE_ATTR:
                DeviceAttributeCondition deviceAttributeCondition = JSONObject.parseObject(this.conditionJson, DeviceAttributeCondition.class);
                return deviceAttributeCondition;
            case TIME_POINT:
                TimePointCondition timePointCondition = JSONObject.parseObject(this.conditionJson, TimePointCondition.class);
                return timePointCondition;
            case TIME_RANGE:
                TimeRangeCondition timeRangeCondition = JSONObject.parseObject(this.conditionJson, TimeRangeCondition.class);
                return timeRangeCondition;
            default:
                return null;
        }
    }

}
