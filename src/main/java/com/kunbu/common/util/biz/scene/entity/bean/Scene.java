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

    public static final Integer SCENE_TYPE_MANUL = 0;
    public static final Integer SCENE_TYPE_AUTO = 1;

    private Long id;

    /** 0-手动 1-自动 */
    private Integer sceneType;

    private String sceneName;

    private String sceneIcon;

    private String sceneDesc;

    /** 是否开启 0-关 1-开 */
    private Integer autoSwitch;

    /** 1-同时满足才触发 */
    private Integer triggerAll;

    /** 1-同时满足条件才执行 */
    private Integer conditionAll;

    private String userUuid;

    private Integer sort;

    private Date createTime;

    private Date updateTime;
}
