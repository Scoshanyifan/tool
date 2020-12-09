package com.kunbu.common.util.biz.scene.entity.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kunbu
 * @date 2020/12/9 10:08
 **/
@Data
public class Scene implements Serializable {

    private Long id;
    /** 0-手动 1-自动 */
    private Integer sceneType;
    /** 场景名称 */
    private String sceneName;
    /** 场景描述 */
    private String sceneDesc;
    /** 是否开启 0-关 1-开 */
    private Integer sceneSwitch;
    /** 场景图标 */
    private String sceneIcon;

    private Integer sort;

    /** 1-同时满足才触发 */
    private Integer triggerAll;

    /** 1-同时满足条件才执行 */
    private Integer conditionAll;

    private String userUuid;

    private String deviceUuid;

    private String productKey;

    private Date createTime;

    private Date updateTime;
}
