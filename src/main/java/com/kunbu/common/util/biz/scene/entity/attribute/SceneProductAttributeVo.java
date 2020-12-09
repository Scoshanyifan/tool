package com.kunbu.common.util.biz.scene.entity.attribute;

import lombok.Data;

/**
 * App需要根据数据类型，自行解析数据并展开
 *      1. 数值：设置滚轮，高于/低于某个值
 *      2. 布尔/枚举：将取值范围展开，如开机/关机，各种模式
 *
 * @author kunbu
 * @date 2020/12/8 16:42
 **/
@Data
public class SceneProductAttributeVo {

    private String attributeName;

    private String attributeKey;

    private Integer dataType;

    /** 此处取值范围基于产品属性原始范围，在此基础上可调整，{"0":"关闭","1":"打开"} */
    private String dataValue;

}
