package com.kunbu.common.util.biz.scene.entity.dto.condition;

import com.kunbu.common.util.biz.scene.constant.SceneConditionTypeEnum;
import lombok.Data;

/**
 * @author kunbu
 * @date 2020/12/7 15:41
 **/
@Data
public class DeviceAttributeCondition implements SceneConditionBase {

    /** 比较类型（大于） */
    private Integer compareType;
    /** 属性条件（温度-20） */
    private Object checkValue;
    /** 属性key */
    private String attributeKey;
    /** 数据类型：1：布尔，2：整数，3：浮点，4：字符串，5：枚举 */
    private Integer dataType;

    @Override
    public SceneConditionTypeEnum getConditionTypeEnum() {
        return SceneConditionTypeEnum.DEVICE_ATTR;
    }
}
