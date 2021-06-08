package com.kunbu.common.util.biz.scene.entity.dto.condition;

import com.kunbu.common.util.biz.scene.constant.SceneConditionTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class TimeRangeCondition implements SceneConditionBase {

    private List<String> rangeCron;

    private String startTime;

    private String endTime;

    private Integer timeType;

    /** 自定义一周的哪几天（周日-周六 >>> 1-7） */
    private List<Integer> dayOfWeek;

    @Override
    public SceneConditionTypeEnum getConditionTypeEnum() {
        return SceneConditionTypeEnum.TIME_RANGE;
    }
}
