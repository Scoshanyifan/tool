package com.kunbu.common.util.biz.scene.entity.bean;

import lombok.Data;

import java.util.Map;

/**
 * @author kunbu
 * @date 2020/12/7 11:42
 **/
@Data
public class DeviceMqtt {

    private String mqttKey;
    private String mqttUuid;
    private String mqttType;

    /**
     * "data": {
     *     "@type": "java.util.HashMap",
     *     "ReserveLeftTime": 16,
     *     "CustomMode": 1,
     *     "Mode": 1,
     *     "RGBColor": 32,
     *     "status": 0,
     *     "undefined": 80,
     *     "Powerstate": 1
     *   }
     **/
    private Map<String, Object> data;

}
