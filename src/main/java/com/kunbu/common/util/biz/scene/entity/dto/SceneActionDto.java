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

    private String productKey;

    private String deviceUuid;

    private Integer sort;

    /** 1-延时，2-设备属性，3-手机通知 */
    private Integer actionType;

    /** 延时长度 */
    private Integer delaySecond;

    /** 设备属性 */
    private String attributeKey;
    /** 期望执行结果，基于属性范围（开关-1，温度-30，设置模式-2） */
    private String expectValue;
}
