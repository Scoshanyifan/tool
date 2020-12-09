package com.kunbu.common.util.biz.scene.entity.action;

import com.kunbu.common.util.biz.scene.constant.SceneActionTypeEnum;
import com.kunbu.common.util.biz.scene.entity.action.SceneActionBase;
import lombok.Data;

/**
 * @author kunbu
 * @date 2020/12/7 10:24
 **/
@Data
public class NotifySceneAction implements SceneActionBase {

    private Long sceneId;

    private Integer sort;

    /** 1-phone 2-app */
    private Integer notifyType;

    private String content;

    private String phone;

    public NotifySceneAction() {
    }

    public NotifySceneAction(String content, String phone) {
        this.content = content;
        this.phone = phone;
    }

    @Override
    public SceneActionTypeEnum getActionTypeEnum() {
        return SceneActionTypeEnum.DEVICE_ATTR;
    }

    @Override
    public Long getSceneId() {
        return sceneId;
    }

    @Override
    public Integer getSort() {
        return sort;
    }
}
