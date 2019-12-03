package com.kunbu.common.util.tool.sql.mongo.demo;

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
public class OrderMongo {

    @Id
    private String id;
    private Date createTime;
    private String orderNum;
    private String clientName;
    private List<GoodMongo> items;

}
