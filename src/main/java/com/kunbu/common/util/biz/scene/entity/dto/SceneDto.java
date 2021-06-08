package com.kunbu.common.util.biz.scene.entity.dto;

import com.kunbu.common.util.biz.scene.entity.bean.SceneCondition;
import com.kunbu.common.util.biz.scene.entity.dto.SceneActionDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author kunbu
 * @date 2020/12/9 10:08
 **/
public class SceneDto implements Serializable {

    private Long id;
    /** 0-手动 1-自动 */
    private Integer sceneType;
    /** 场景名称 */
    private String sceneName;
    /** 场景描述 */
    private String sceneDesc;
    /** 自动化开关 0-关 1-开 */
    private Integer autoSwitch;
    /** 场景图标 */
    private String sceneIcon;

    private Integer sort;

    /** true-同时满足才触发 */
    private Boolean triggerAll;
    private List<SceneConditionDto> triggerList;

    /** true-同时满足条件才执行 */
    private Boolean conditionAll;
    private List<SceneConditionDto> conditionList;

    private List<SceneActionDto> actionList;


}
