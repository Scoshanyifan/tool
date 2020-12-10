package com.kunbu.common.util.biz.scene.entity.dto.condition;

import com.kunbu.common.util.biz.scene.constant.SceneConditionTypeEnum;
import lombok.Data;

/**
 * trigger阶段：时间点
 * condition阶段：时间段/时间点
 *
 * @author kunbu
 * @date 2020/12/7 14:58
 **/
@Data
public class CronCondition implements SceneConditionBase {

    private String cronStr;

    @Override
    public SceneConditionTypeEnum getConditionTypeEnum() {
        return SceneConditionTypeEnum.CRON;
    }
}
