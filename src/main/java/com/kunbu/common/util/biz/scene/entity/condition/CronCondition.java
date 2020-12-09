package com.kunbu.common.util.biz.scene.entity.condition;

import com.kunbu.common.util.biz.scene.constant.SceneConditionTypeEnum;
import lombok.Data;

/**
 * trigger阶段只支持瞬时
 * condition阶段可以是时间段
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
