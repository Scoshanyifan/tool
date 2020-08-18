package com.kunbu.common.util.mongo;

import com.kunbu.common.util.tool.sql.mongo.common.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @project: bucks
 * @author: kunbu
 * @create: 2019-09-04 16:12
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoWriteTest {

    private static final Logger logger = LoggerFactory.getLogger(MongoWriteTest.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testFindAndModify() {
        // findAndModify的原子性
        Order order = mongoTemplate.findAndModify(
                new Query(Criteria.where("orderNum").is("2019120201").and("status").is(Order.ORDER_STATUS_INIT)),
                new Update().set("status", Order.ORDER_STATUS_TAKE).set("createTime", new Date()),
                Order.class
        );

        logger.info(">>> find and modify:{}", order);
    }

    @Test
    public void testUpdate() {
        // update one

        // update multi
    }

}

