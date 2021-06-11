package com.kunbu.common.util.biz.rabbitmq.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@TableName("rabbitmq_event")
public class RabbitmqEvent {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("json_data")
    private String jsonData;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField("correlation_id")
    private String correlationId;

    @TableField("status")
    private Integer status;

    @TableField("count")
    private Integer count;

    public RabbitmqEvent(String jsonData, String correlationId) {
        this.jsonData = jsonData;
        this.correlationId = correlationId;
        this.createTime = LocalDateTime.now();
        this.status = 0;
    }
}
