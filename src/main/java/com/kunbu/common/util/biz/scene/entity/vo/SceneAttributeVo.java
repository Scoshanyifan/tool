package com.kunbu.common.util.biz.scene.entity.vo;

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
public class SceneAttributeVo {

    private Long id;

    private String productKey;

    private Integer sort;

    private Integer sceneType;

    private String autoName;

    private Integer autoType;

    private Integer dataType;

    private Long attributeId;

    private String attributeName;

    private String attributeValue;

}
