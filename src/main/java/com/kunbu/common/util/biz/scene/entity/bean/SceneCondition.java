package com.kunbu.common.util.biz.scene.entity.bean;

import com.alibaba.fastjson.JSONObject;
import com.kunbu.common.util.biz.scene.constant.SceneConditionTypeEnum;
import com.kunbu.common.util.biz.scene.entity.dto.condition.SceneConditionBase;
import com.kunbu.common.util.biz.scene.entity.dto.condition.CronCondition;
import com.kunbu.common.util.biz.scene.entity.dto.condition.DeviceAttributeCondition;
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

    /** 1-cron 2-设备属性 */
    private Integer conditionType;

    private String userUuid;

    private String productKey;

    private String deviceUuid;

    private Integer sort;

    /** 具体条件，区分时间和设备属性 */
    private String conditionJson;

    private Date createTime;

    private Date updateTime;

    public SceneConditionBase getCondition() {
       SceneConditionTypeEnum sceneConditionTypeEnum = SceneConditionTypeEnum.of(conditionType);
        switch (sceneConditionTypeEnum) {
            case DEVICE_ATTR:
                DeviceAttributeCondition deviceAttributeCondition = JSONObject.parseObject(this.conditionJson, DeviceAttributeCondition.class);
                return deviceAttributeCondition;
            case CRON:
                CronCondition cronCondition = JSONObject.parseObject(this.conditionJson, CronCondition.class);
                return cronCondition;
            default:
                return null;
        }
    }

}
