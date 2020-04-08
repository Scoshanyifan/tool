package com.kunbu.common.util.tool.sql.mongo.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-12-02 16:43
 **/
@Data
@Document(collection = "order")
public class Order {

    public static final Integer ORDER_STATUS_INIT = 1;
    public static final Integer ORDER_STATUS_FORBID = 2;
    public static final Integer ORDER_STATUS_TAKE = 3;
    public static final Integer ORDER_STATUS_DELIVER = 4;
    public static final Integer ORDER_STATUS_FINISHED = 5;

    @Id
    private String id;
    private Date createTime;
    private String orderNum;
    private String clientName;
    private Integer status;
    private List<Good> items;

}
