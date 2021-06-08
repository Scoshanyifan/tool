package com.kunbu.common.util.biz.scene.entity.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author kunbu
 * @date 2020/12/8 11:58
 **/
@Data
public class SceneLogMongo {

    private Long id;

    private Long sceneId;

    /** 0-失败 1-部分成功 2-全部成功 */
    private Integer result;

    private Date createTime;

}
