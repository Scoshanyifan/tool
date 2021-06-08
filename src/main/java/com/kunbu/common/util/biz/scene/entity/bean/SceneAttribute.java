package com.kunbu.common.util.biz.scene.entity.bean;

import lombok.Data;

import java.util.Date;

/**
 * open平台设置自动化属性
 *
 * 开发者基于nodeType进行预设：
 *      trigger/condition阶段：设置的设备属性，如果净水器温度低于26（大于，小于，等于）
 *      action阶段：设备可以执行的动作，把净水器调到睡眠模式（等于）
 *
 * @author kunbu
 * @date 2020/12/4 16:29
 **/
@Data
public class SceneAttribute {

    private Long id;

    private String productKey;

    private Integer sort;

    /** 类别：1-if（trigger/condition），2-then（action） */
    private Integer type;

    /** if-类别下的自动化类型：0-瞬时类，1-状态类（需要基于前一次状态判断，如温度高于20度时，必须19->20才触发，而不是21->20也触发） */
    private Integer autoType;

    /** 自动化属性名称 */
    private String autoName;

    /** 数据类型：1：布尔，2：整数，3：浮点，4：字符串，5：枚举 */
    private Integer dataType;

    /** 原始属性id */
    private Long attributeId;

    private String attributeKey;

    private String attributeName;

    /**
     * 布尔/枚举类型：选择具体item作为单独条件
     *      布尔：{"0":"关闭"} / {"0":"关闭","1":"打开"}
     *      枚举：{"0":"正常风"} / {"0":"正常风","1":"睡眠风","2":"自然风"}
     *
     * 数值取值范围基于产品属性原始范围，在此基础上调整
     *      数值：{
     *              "dataRangeMin":-50,
     *              "unit":"℃",
     *              "step":1,
     *              "initialValue":0,
     *              "dataRangeMax":70,
     *              "roller":true,      // 滚轴否，需要target
     *              "target":null,
     *              "compareType":1
     *           }
     *
     **/
    private String attributeValue;

    /** 自动化属性状态，0-删除，1-使用，2-停用 */
    private Integer state;

    private Date createTime;

    private Date updateTime;

}
