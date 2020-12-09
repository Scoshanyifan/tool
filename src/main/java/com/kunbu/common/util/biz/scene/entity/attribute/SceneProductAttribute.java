package com.kunbu.common.util.biz.scene.entity.attribute;

import lombok.Data;

/**
 * 开发者基于nodeType进行预设：
 *      trigger/condition阶段：设置的设备属性，如果净水器温度低于26（大于，小于，等于）
 *      action阶段：设备可以执行的动作，把净水器调到睡眠模式（等于）
 *
 * @author kunbu
 * @date 2020/12/4 16:29
 **/
@Data
public class SceneProductAttribute {

    private Long id;

    /** 原始属性id */
    private Long attributeId;

    private String productKey;

    private String attributeKey;

    private String attributeName;

    private Integer dataType;

    /** 1-trigger/condition 2-action */
    private Integer nodeType;

    /**
     * 此处取值范围基于产品属性原始范围，在此基础上可调整
     *
     * 布尔：{"0":"关闭","1":"打开"}
     * 枚举：{"0":"正常风","1":"睡眠风","2":"自然风"}
     * 数值：{"dataRangeMin":-50,"unit":"℃","step":1,"initialValue":0,"dataRangeMax":70,"roller":true,"target":null,"compareType":1}
     *
     **/
    private String dataValue;

}
