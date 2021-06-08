package com.kunbu.common.util.biz.scene.entity.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author kunbu
 * @date 2020/12/4 16:08
 **/
@Data
public class SceneAction {

    public static final Integer DELAY_MIN = 1;
    public static final Integer DELAY_MAX = 3600;

    private Long id;

    private Long sceneId;

    private String userUuid;

    private Integer sort;

    /** 1-延时 2-设备属性 3-通知*/
    private Integer actionType;

    /** 1-3600 */
    private Integer delaySecond;

    private String content;

    private String productKey;

    private String deviceUuid;

    private Long sceneAttributeId;

    private String attributeKey;

    private String expectValue;

    private Integer dataType;

    private Date createTime;

    private Date updateTime;


}
