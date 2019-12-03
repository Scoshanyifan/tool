package com.kunbu.common.util.tool.sql.mongo.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-12-02 17:23
 **/
@Data
@AllArgsConstructor
public class GoodMongo {
    private String goodCode;
    private Integer quantity;
    private Double price;
}
