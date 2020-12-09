package com.kunbu.common.util.biz.scene.entity.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author kunbu
 * @date 2020/12/7 11:58
 **/
@Data
public class Device {

    private Long id;

    private String deviceUuid;

    private String deviceSecret;

    private Long productId;

    private String productKey;

    private String productName;

    public String productIcon;

    private String deviceVersion;

    private int deviceCode;

    private String deviceSn;

    private String lastIp;

    private int areaId;

    private String province;

    private String city;

    private String county;

    private String address;

    private Date createTime;

    private Date updateTime;

    private Date versionTime;

    private Date activeTime;

    private int onlineDuration;

    private int offlineDuration;

    private int status;

    private int deviceType;

    private String userUuid;
}
