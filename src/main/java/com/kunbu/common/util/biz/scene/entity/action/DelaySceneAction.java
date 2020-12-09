package com.kunbu.common.util.biz.scene.entity.action;

import com.kunbu.common.util.biz.scene.constant.SceneActionTypeEnum;
import com.kunbu.common.util.biz.scene.entity.action.SceneActionBase;
import lombok.Data;

/**
 * @author kunbu
 * @date 2020/12/7 10:24
 **/
@Data
public class DelaySceneAction implements SceneActionBase {

    private Long sceneId;

    private Integer sort;

    private Long delaySecond;

    public DelaySceneAction() {
    }

    public DelaySceneAction(Long delaySecond) {
        this.delaySecond = delaySecond;
    }

    @Override
    public SceneActionTypeEnum getActionTypeEnum() {
        return SceneActionTypeEnum.DELAY;
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
