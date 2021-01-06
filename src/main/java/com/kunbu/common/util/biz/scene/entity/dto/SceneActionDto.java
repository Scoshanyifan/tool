package com.kunbu.common.util.biz.scene.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kunbu
 * @date 2020/12/4 16:08
 **/
@Data
public class SceneActionDto implements Serializable {

    private Long id;

    private Integer sort;

    /** 1-延时，2-设备属性，3-手机通知 */
    private Integer actionType;

    /** 1-延时长度 */
    private Integer delaySecond;

    /** 3-手机通知内容 */
    private String content;

    /** 2-动作属性 sceneAttributeId */
    private Long sceneAttributeId;
    /** 属性key */
    private String attributeKey;

    private String productKey;

    private String deviceUuid;
    /** 期望执行结果，基于属性范围（开关-1，温度-30，设置模式-2） */
    private String expectValue;

    private Integer dataType;

    /** 1-标准 2-透传 */
    private Integer productDataForm;


}
