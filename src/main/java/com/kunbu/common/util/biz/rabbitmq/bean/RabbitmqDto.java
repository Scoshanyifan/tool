package com.kunbu.common.util.biz.rabbitmq.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RabbitmqDto implements Serializable {

    private String vehicleNo;
    private Integer serverAreaId;
    private Integer passId;
    private String correlationId;
    private String inOutFlag;
    private Integer vehicleType;
    private String vehicleColor;
    private String plateColor;

    /**
     * spring 默认是用jackson解析json，使用localDateTime需要用到，不能使用时间戳，因为内部是数组结构
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
