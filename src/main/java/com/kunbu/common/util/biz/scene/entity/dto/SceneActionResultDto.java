package com.kunbu.common.util.biz.scene.entity.dto;

import com.kunbu.common.util.biz.scene.entity.dto.action.SceneActionBase;
import lombok.Data;

/**
 * @author kunbu
 * @date 2020/12/8 11:32
 **/
@Data
public class SceneActionResultDto {

    private Boolean success;

    private String failureReason;

    private SceneActionBase sceneActionBase;

}
