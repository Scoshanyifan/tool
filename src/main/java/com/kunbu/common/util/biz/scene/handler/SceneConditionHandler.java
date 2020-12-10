package com.kunbu.common.util.biz.scene.handler;

import com.kunbu.common.util.biz.scene.entity.bean.DeviceMqtt;
import com.kunbu.common.util.biz.scene.constant.SceneCompareTypeEnum;
import com.kunbu.common.util.biz.scene.constant.SceneConditionTypeEnum;
import com.kunbu.common.util.biz.scene.entity.bean.SceneCondition;
import com.kunbu.common.util.biz.scene.entity.dto.condition.CronCondition;
import com.kunbu.common.util.biz.scene.entity.dto.condition.DeviceAttributeCondition;
import org.quartz.CronExpression;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 1-时间条件 + n-不重复的device属性
 *
 * @author kunbu
 * @date 2020/12/2 11:45
 **/
public class SceneConditionHandler {

    public static boolean checkCondition(DeviceMqtt deviceMqtt, List<SceneCondition> sceneConditionList) {
        boolean check;
        for (SceneCondition sceneCondition : sceneConditionList) {
            SceneConditionTypeEnum sceneConditionTypeEnum = SceneConditionTypeEnum.of(sceneCondition.getConditionType());
            switch (sceneConditionTypeEnum) {
                case DEVICE_ATTR:
                    DeviceAttributeCondition deviceAttributeCondition = (DeviceAttributeCondition) sceneCondition.getCondition();
                    check = checkDeviceAttribute(deviceMqtt.getData(), deviceAttributeCondition);
                    break;
                case CRON:
                    CronCondition cronCondition = (CronCondition) sceneCondition.getCondition();
                    check = checkCronExpression(new Date(), cronCondition);
                    break;
                default:
                    check = false;
                    break;
            }
            if (!check) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验设备属性条件
     *
     **/
    private static boolean checkDeviceAttribute(Map<String, Object> data, DeviceAttributeCondition deviceAttributeCondition) {
        try {
            String attributeKey = deviceAttributeCondition.getAttributeKey();
            if (data != null && data.containsKey(attributeKey)) {
                Object value = data.get(attributeKey);
                if (value != null) {
                    SceneCompareTypeEnum sceneCompareTypeEnum = SceneCompareTypeEnum.of(deviceAttributeCondition.getCompareType());
                    switch (sceneCompareTypeEnum) {
                        case EQUALS:
                            // boolean number enum
                            if (value.toString().equals(deviceAttributeCondition.getCheckValue().toString())) {
                                return true;
                            }
                            break;
                        case LESS_THAN:
                        case GRATER_THAN:
                            Double valueDouble = Double.valueOf(value.toString());
                            Double checkValueDouble = Double.valueOf(deviceAttributeCondition.getCheckValue().toString());
                            if (sceneCompareTypeEnum.equals(SceneCompareTypeEnum.LESS_THAN) && valueDouble.compareTo(checkValueDouble) < 0) {
                                return true;
                            }
                            if (sceneCompareTypeEnum.equals(SceneCompareTypeEnum.GRATER_THAN) && valueDouble.compareTo(checkValueDouble) > 0) {
                                return true;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            // TODO ERROR
        }
        return false;
    }

    /**
     * 校验时间条件
     *
     * @param date
     * @param cronCondition
     **/
    private static boolean checkCronExpression(Date date, CronCondition cronCondition) {
        try {
            CronExpression cronExpression = new CronExpression(cronCondition.getCronStr());
            return cronExpression.isSatisfiedBy(date);
        } catch (Exception e) {
            // TODO ERROR
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        CronCondition cronCondition = new CronCondition();
        cronCondition.setCronStr("* * 9-10 * * ?");
        System.out.println(checkCronExpression(new Date(), cronCondition));
    }

}
