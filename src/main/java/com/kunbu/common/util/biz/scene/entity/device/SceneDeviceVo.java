package com.kunbu.common.util.biz.scene.entity.device;

import lombok.Data;

/**
 * @author kunbu
 * @date 2020/12/8 15:23
 **/
@Data
public class SceneDeviceVo {

    private String userUuid;

    private String deviceUuid;

    private String deviceName;

    private String productKey;

    private Long productId;

    private String productIcon;
    /** 4-在线 5-离线（此状态下不可选择属性）*/
    private Integer status;

}
