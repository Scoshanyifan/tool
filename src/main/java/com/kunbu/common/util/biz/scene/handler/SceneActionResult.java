package com.kunbu.common.util.biz.scene.handler;

import com.kunbu.common.util.biz.scene.entity.action.SceneActionBase;
import lombok.Data;

/**
 * @author kunbu
 * @date 2020/12/8 11:32
 **/
@Data
public class SceneActionResult {

    private Boolean success;

    private String failureReason;

    private SceneActionBase sceneActionBase;

}
