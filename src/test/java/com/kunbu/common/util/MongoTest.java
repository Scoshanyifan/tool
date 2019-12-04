package com.kunbu.common.util;

import com.google.common.collect.Lists;
import com.kunbu.common.util.basic.DateFormatUtil;
import com.kunbu.common.util.tool.sql.mongo.MongoBsonAggregationUtil;
import com.kunbu.common.util.tool.sql.mongo.MongoBsonQueryUtil;
import com.kunbu.common.util.tool.sql.mongo.MongoCriteriaUtil;
import com.kunbu.common.util.tool.sql.mongo.demo.GoodMongo;
import com.kunbu.common.util.tool.sql.mongo.demo.OrderMongo;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @project: bucks
 * @author: kunbu
 * @create: 2019-09-04 16:12
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTest {

    private static final Logger logger = LoggerFactory.getLogger(MongoTest.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void initData() {
        List<OrderMongo> orderList = Lists.newArrayList();
        long time = new Date().getTime();
        for (int i = 1; i <= 8; i++) {
            OrderMongo order = new OrderMongo();
            order.setClientName(new Random().nextBoolean() ? "kunbu" : "scosyf");
            order.setCreateTime(new Date(time - new Random().nextInt(100) * 1000 * 3600 * i));
            order.setOrderNum("201912020" + i);
            order.setItems(Lists.newArrayList(
                    new GoodMongo("GD1" + new Random().nextInt(10), 1 + i, 0.5 * i + 2),
                    new GoodMongo("GD2" + new Random().nextInt(10), 2 + i, 0.5 * i + 3)));
            orderList.add(order);
        }
        mongoTemplate.insert(orderList, OrderMongo.class);
    }

    /**
     * https://www.cnblogs.com/woshimrf/p/mongodb-pagenation-performance.html
     **/
    @Test
    public void testPage() {
        int pageNum = 1;
        int pageSize = 5;

        // 1. 常规分页
        Query query = new Query(Criteria.where("clientName").is("kunbu"))
                .with(Sort.by(Sort.Direction.DESC, "createTime"))
                .skip((pageNum - 1) * pageSize)
                .limit(pageSize);
        query.fields().include("createTime");
        logger.info(">>> {}", query.toString());
        List<Map> result = mongoTemplate.find(query, Map.class, "order");
        result.stream().forEach(log -> logger.info(">>> page_1: {}", log));


        // 2. 性能分页：
        // 按id或时间排序后，取前一页最后一条记录的id或时间戳作为条件，大于或小于这个值进行limit（第一次默认返回第一页）
//        String id = (String) result.get(result.size() - 1).get("_id");
        // 这里按时间最新的排序，即的最后一条记录的时间戳作为查询条件，小于该时间的接下去10条
        Date time = (Date) result.get(result.size() - 1).get("createTime");
        // 这里用了lte而不是lt，因为如果同一时间点有多条数据，那lt存在少取的可能，为了保险，用lte，虽然会存在前后页重复数据，但是确保都展示 TODO
        Query query2 = new Query(Criteria.where("clientName").is("kunbu").and("createTime").lte(time))
                .with(Sort.by(Sort.Direction.DESC, "createTime"))
                .limit(pageSize);
        query2.fields().include("createTime");
        logger.info(">>> {}", query2.toString());
        result = mongoTemplate.find(query2, Map.class, "order");
        result.stream().forEach(log -> logger.info(">>> page_2: {}", log));
    }

    /**
     * 查询有2种方式：
     *  1. Query (Criteria criteria)
     *  2. BasicQuery(DBObject queryObject) 即Bson形式
     *
     * https://www.cnblogs.com/fqybzhangji/p/9887922.html
     **/
    @Test
    public void testQuery() {
        // 1. Query
        Criteria criteria = Criteria.where("field").not().gt("value1").and("field").lt("value2");
        // 报错 InvalidMongoDbApiUsageException, you can't add a second 'field' expression specified as 'field
        Query query = new Query(criteria);
        logger.info(">>> query:{}", query);


        // 2. BasicQuery
        BasicDBObject find = MongoBsonQueryUtil.and(
                MongoBsonQueryUtil.is("clientName", "kunbu"), MongoBsonQueryUtil.is("orderNum", "2019120201"));
        BasicDBObject project = MongoBsonQueryUtil.project(
                true, "clientName", "orderNum", "createTime");

        Query basicQuery = new BasicQuery(find.toString(), project.toString());
        List<Map> results = mongoTemplate.find(basicQuery, Map.class, "order");
        results.stream().forEach(log -> logger.info(">>> {}", log));
    }


    /**
     * 聚合查询
     *
     * 1. 使用API聚合（不能实现复杂逻辑）
     * Aggregation agg = newAggregation(
     *      pipelineOP1(),
     *      pipelineOP2(),
     *      pipelineOPn()
     * );
     *
     * 2. Bson原生写法
     * https://blog.csdn.net/congcong68/article/details/52821159
     *
     *
     * 时区的坑：https://blog.csdn.net/zzq900503/article/details/85606222
     *
     **/
    @Test
    public void testAggregation() {
        Date start = DateFormatUtil.parse("2019-11-01 16:00:00", DateFormatUtil.DEFAULT_DATE_PATTERN);
        Date end = DateFormatUtil.parse("2019-12-30 18:00:00", DateFormatUtil.DEFAULT_DATE_PATTERN);

        // 1. Aggregation（api不支持group复杂写法）
        Criteria clientNameCriteria = MongoCriteriaUtil.strIs("clientName", "kunbu");
        Criteria timeCriteria = MongoCriteriaUtil.dateCompare("createTime", start, end, false);

        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.project(),
                Aggregation.match(clientNameCriteria),
                Aggregation.match(timeCriteria),
                // 把元素展开
                Aggregation.unwind("items"),
                // api的group只能用现有字段，而原生bson可以进行函数处理
                Aggregation.group("createTime", "items.goodCode")
                        .sum("items.quantity").as("count"),
                Aggregation.project("count")
        );
        logger.info(">>> aggregation:{}", aggregation);

        AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(aggregation, OrderMongo.class, Document.class);
        logger.info(">>> rawResults:{}", aggregationResults.getRawResults());
        List<Document> mappedResults = aggregationResults.getMappedResults();
        mappedResults.stream().forEach(x -> logger.info(">>> result:{}", x));


        // 2. Bson
        /**
         * 带统计的复杂聚合：时间段内，按月维度统计items中每种good的数量，并且最终每种good数量小于10
         *
         * 数据：
         * {
         *     "_id" : ObjectId("5de5c393595c87400482f434"),
         *     "createTime" : ISODate("2019-11-30T14:08:19.416Z"),
         *     "orderNum" : "2019120201",
         *     "clientName" : "kunbu",
         *     "items" : [
         *         {
         *             "goodCode" : "GD10",
         *             "quantity" : 2,
         *             "price" : 2.5
         *         },
         *         {
         *             "goodCode" : "GD22",
         *             "quantity" : 3,
         *             "price" : 3.5
         *         }
         *     ],
         * }
         * 聚合：
         * db.order.aggregate([
         *     {$match:{
         *             clientName:'kunbu',
         *             createTime:{
         *                 $gt:ISODate("2019-11-01T16:00:00.000Z"),
         *                 $lt:ISODate("2019-12-30T18:00:00.000Z")
         *                 }
         *             }
         *     },
         *     {$unwind:'$items'},
         *     {$group:{
         *             _id:{
         *                 month:{$month:{$add:['$createTime',8]}},
         *                 goodCode:'$items.goodCode'
         *             },
         *             count:{$sum:'$items.quantity'}
         *         }
         *     },
         *     {$match:{
         *         count:{$lt:10}}
         *     }
         * ])
         * 结果：
         * {
         *     "_id" : {
         *         "month" : 12,
         *         "goodCode" : "GD10"
         *     },
         *     "count" : 9
         * }
         * {
         *     "_id" : {
         *         "month" : 11,
         *         "goodCode" : "GD11"
         *     },
         *     "count" : 8
         * }
         * ... ...
         *
         **/
        // match
        BasicDBObject match = MongoBsonQueryUtil.and(
                // new BasicDBObject("createTime", new BasicDBObject("$gt", start))
                MongoBsonQueryUtil.gt("createTime", start, false),
                MongoBsonQueryUtil.lt("createTime", end, false),
                MongoBsonQueryUtil.is("clientName", "kunbu"));

        // unwind
        BasicDBObject unwind = new BasicDBObject("$unwind", "$items");

        // group
        BasicDBObject _id = new BasicDBObject();
        // _id.put("month", new BasicDBObject("$month", new BasicDBObject("$add", new Object[]{"$createTime", 8})));
        _id.put("month", new BasicDBObject("$month", MongoBsonAggregationUtil.add("createTime", 8)));
        _id.put("goodCode", "$items.goodCode");
        BasicDBObject group = new BasicDBObject();
        group.put("_id", _id);
        group.put("count", new BasicDBObject("$sum", "$items.quantity"));

        // match
        BasicDBObject match2 = MongoBsonQueryUtil.lt("count", 10, false);

        //按管道的顺序执行
        List<BasicDBObject> pipelines = Lists.newArrayList(
                new BasicDBObject("$match", match),
                unwind,
                new BasicDBObject("$group", group),
                new BasicDBObject("$match", match2)
        );
        pipelines.stream().forEach(p -> logger.info("pipeline:{}", p));

        AggregateIterable<Document> docs = mongoTemplate.getCollection("order").aggregate(pipelines);
        for (Document doc : docs) {
            logger.info(">>> doc:{}", doc);
        }
    }

    @Test
    public void testBucket() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("items"),
                Aggregation
                        // 分组字段
                        .bucket("items.price")
                        // 分组范围（结果中的id表示范围：0-3，3-6，6-正无穷）
                        .withBoundaries(0, 3, 6)
                        // 类似switch最后的default
                        .withDefaultBucket("default")
                        // 输出内容，good种类数（含重复，因为是每个分组下）
                        .andOutput("items.goodCode").count().as("goodCount")
                        // 产品总量
                        .andOutput("items.quantity").sum().as("sum")
                        // push() 展开goodCode
                        .andOutput("items.goodCode").push().as("goodCodes")
        );
        AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(aggregation, OrderMongo.class, Document.class);
        // Document{{_id=0, goodCount=1, sum=2, goodCodes=[GD10]}}
        aggregationResults.getMappedResults().stream().forEach(x -> logger.info(">>> result:{}", x));
    }
}

