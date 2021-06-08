package com.kunbu.common.util.mongo;

import com.google.common.collect.Lists;
import com.kunbu.common.util.tool.sql.mongo.common.Good;
import com.kunbu.common.util.tool.sql.mongo.common.Order;
import com.mongodb.client.result.UpdateResult;
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
import java.util.List;
import java.util.Random;

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

    @Test
    public void testSave() {
        Order order = new Order();
        order.setClientName(new Random().nextBoolean() ? "kunbu" : "scosyf");
        mongoTemplate.save(order);
        logger.info(">>> order:{}", order);
    }

    /** ==================== 内嵌操作 ====================== */

    @Test
    public void testAddEmbedList() {
        List<Good> items = Lists.newArrayList(new Good("AddToSetBuilder", 1, 3.14));
        UpdateResult result = mongoTemplate.upsert(
                new Query(Criteria.where("orderNum").is("2019120201")),
                new Update().new AddToSetBuilder("items").each(items),
                Order.class
        );
        logger.info(">>> AddToSetBuilder:{}", result);

        Good item = new Good("addToSet", 2, 0d);
        result = mongoTemplate.upsert(
                new Query(Criteria.where("orderNum").is("2019120201")),
                new Update().addToSet("items", item),
                Order.class
        );
        logger.info(">>> addToSet:{}", result);

        /** 如果数据已经存在，push操作会插入一条一样的数据 */
        item = new Good("push", 3, 1d);
        result = mongoTemplate.upsert(
                new Query(Criteria.where("orderNum").is("2019120201")),
                new Update().push("items", item),
                Order.class
        );
        logger.info(">>> push:{}", result);
    }

    @Test
    public void testUpdateEmbedList() {
        UpdateResult result = mongoTemplate.updateFirst(
                new Query(Criteria.where("_id").is("601921d7024bfd6c7fab2e50").and("items.goodCode").is("push")),
                new Update().set("items.$.price", 3.63).set("items.$.quantity", 5999),
                Order.class
        );
        logger.info(">>> testUpdateEmbedList:{}", result);
        Order order = mongoTemplate.findOne(
                new Query(Criteria.where("_id").is("601921d7024bfd6c7fab2e50")),
                Order.class
        );
        logger.info(">>> order:{}", order);
    }

    @Test
    public void testRemoveEmbedList() {
        UpdateResult result = mongoTemplate.upsert(
                new Query(Criteria.where("orderNum").is("2019120201")),
                new Update().pull("items", new Good("xxx", 1, 34.2)),
                Order.class
        );
        logger.info(">>> testRemoveEmbedList:{}", result);
    }


}

