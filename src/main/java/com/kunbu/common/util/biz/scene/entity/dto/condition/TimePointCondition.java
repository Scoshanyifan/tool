package com.kunbu.common.util.biz.scene.entity.dto.condition;

import com.kunbu.common.util.biz.scene.constant.SceneConditionTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class TimePointCondition implements SceneConditionBase {

    /** 1-时间点 */
    private String pointCron;
    /** 08-23 */
    private String pointTime;

    /** 1-一次 2-工作日 3-周末 4-每天 5-自定义周几*/
    private Integer timeType;

    /** 自定义一周的哪几天（周日-周六 >>> 1-7） */
    private List<Integer> dayOfWeek;

    @Override
    public SceneConditionTypeEnum getConditionTypeEnum() {
        return SceneConditionTypeEnum.TIME_POINT;
    }
}
