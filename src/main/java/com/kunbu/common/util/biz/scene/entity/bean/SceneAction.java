package com.kunbu.common.util.biz.scene.entity.bean;

import com.alibaba.fastjson.JSONObject;
import com.kunbu.common.util.biz.scene.constant.SceneActionTypeEnum;
import com.kunbu.common.util.biz.scene.entity.dto.action.SceneActionBase;
import com.kunbu.common.util.biz.scene.entity.dto.action.DelaySceneAction;
import com.kunbu.common.util.biz.scene.entity.dto.action.DeviceSceneAction;
import com.kunbu.common.util.biz.scene.entity.dto.action.NotifySceneAction;
import lombok.Data;

import java.util.Date;

/**
 * @author kunbu
 * @date 2020/12/4 16:08
 **/
@Data
public class SceneAction {

    private Long id;

    private Long sceneId;

    private String userUuid;

    private String productKey;

    private String deviceUuid;

    /** 1-延时 2-设备属性 3-通知*/
    private Integer actionType;

    private Integer sort;

    private String actionJson;

    private Date createTime;

    private Date updateTime;

    public SceneActionBase getAction() {
        SceneActionTypeEnum sceneActionTypeEnum = SceneActionTypeEnum.of(actionType);
        switch (sceneActionTypeEnum) {
            case DEVICE_ATTR:
                DeviceSceneAction deviceAction = JSONObject.parseObject(this.actionJson, DeviceSceneAction.class);
                return deviceAction;
            case DELAY:
                DelaySceneAction delayActionItem = JSONObject.parseObject(this.actionJson, DelaySceneAction.class);
                return delayActionItem;
            case NOTIFY:
                NotifySceneAction notifyActionItem = JSONObject.parseObject(this.actionJson, NotifySceneAction.class);
                return notifyActionItem;
            default:
                return null;
        }
    }

}
