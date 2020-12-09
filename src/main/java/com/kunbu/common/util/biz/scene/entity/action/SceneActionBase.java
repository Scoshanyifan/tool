package com.kunbu.common.util.biz.scene.entity.action;

import com.kunbu.common.util.biz.scene.constant.SceneActionTypeEnum;

public interface SceneActionBase {

    SceneActionTypeEnum getActionTypeEnum();

    Long getSceneId();

    Integer getSort();
}
