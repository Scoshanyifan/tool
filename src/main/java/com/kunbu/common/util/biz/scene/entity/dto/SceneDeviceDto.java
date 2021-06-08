package com.kunbu.common.util.biz.scene.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kunbu
 * @date 2020/12/4 16:08
 **/
@Data
public class SceneDeviceDto implements Serializable {

    private String userUuid;

    private String deviceUuid;

    private String deviceName;

    private Integer productId;

    private String productKey;

    private String productIcon;

    /** 设备状态：4-在线，5-离线 */
    private Integer status;
}
