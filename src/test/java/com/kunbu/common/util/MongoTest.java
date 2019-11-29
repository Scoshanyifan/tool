package com.kunbu.common.util;

import com.google.common.collect.Lists;
import com.kunbu.common.util.basic.DateFormatUtil;
import com.kunbu.common.util.tool.sql.mongo.MongoBsonAggregationUtil;
import com.kunbu.common.util.tool.sql.mongo.MongoBsonQueryUtil;
import com.kunbu.common.util.tool.sql.mongo.MongoCriteriaUtil;
import com.kunbu.common.util.tool.sql.mongo.demo.RequestLog;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import lombok.AllArgsConstructor;
import lombok.Data;
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

    /**
     * https://www.cnblogs.com/woshimrf/p/mongodb-pagenation-performance.html
     **/
    @Test
    public void testPage() {
        int pageNum = 1;
        int pageSize = 5;
        // 1. 常规分页
        Query query = new Query(Criteria.where("methodName").exists(true));
        query.fields().include("createTime");
        query.with(Sort.by(Sort.Direction.DESC, "createTime"))
                .skip((pageNum - 1) * pageSize)
                .limit(pageSize);
        List<Map> result = mongoTemplate.find(query, Map.class, "requestlog");
        result.stream().forEach(log -> logger.info(">>> query1: {}", log));

        // 2. 按id或时间排序后取id或时间戳的最后一条记录，大于或小于这个值进行limit（第一次默认返回第一页）
//        String id = (String) result.get(9).get("_id");
        Date time = (Date) result.get(result.size() - 1).get("createTime");
        Query query2 = new Query(Criteria.where("methodName").exists(true).and("createTime").lt(time));
        query2.fields().include("createTime");
        query2.with(Sort.by(Sort.Direction.DESC, "createTime"))
                .limit(pageSize);
        logger.info(">>> {}", query2.toString());
        result.stream().forEach(log -> logger.info(">>> query2: {}", log));
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
        BasicDBObject find = MongoBsonQueryUtil.or(
                MongoBsonQueryUtil.is("httpStatus", "200"), MongoBsonQueryUtil.is("httpStatus", "500"));
        BasicDBObject find2 = MongoBsonQueryUtil.and(
                MongoBsonQueryUtil.is("httpStatus", "200"), MongoBsonQueryUtil.is("httpMethod", "GET"));
        BasicDBObject project = MongoBsonQueryUtil.project(
                true, "methodName", "httpMethod", "httpStatus");

        Query basicQuery = new BasicQuery(find2.toString(), project.toString());
        List<Map> results = mongoTemplate.find(basicQuery, Map.class, "requestlog");
        results.stream().forEach(log -> logger.info(">>> {}", log));
    }

    /**
     * 1. 使用API聚合
     * Aggregation agg = newAggregation(
     *      pipelineOP1(),
     *      pipelineOP2(),
     *      pipelineOPn()
     * );
     *
     * 2. Bson写法
     * https://blog.csdn.net/congcong68/article/details/52821159
     *
     *
     * 时区的坑：https://blog.csdn.net/zzq900503/article/details/85606222
     *
     **/
    @Test
    public void testAggregation() {
        // 1. Aggregation
        Criteria httpMethodCriteria = MongoCriteriaUtil.strIs("httpMethod", "GET");
        Criteria timeCriteria = MongoCriteriaUtil.dateCompare("createTime",
                DateFormatUtil.parse("2019-09-09 16:00:00", DateFormatUtil.DEFAULT_DATE_PATTERN),
                DateFormatUtil.parse("2019-09-19 18:04:00", DateFormatUtil.DEFAULT_DATE_PATTERN),
                false);

        //统计时间段内，各个方法调用次数
        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.project(),
                Aggregation.match(new Criteria().andOperator(timeCriteria, httpMethodCriteria)),
                Aggregation.group("methodName", "url").count().as("count")
        );
        logger.info(">>> aggregation:{}", aggregation);
        // 查询
        AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(aggregation, RequestLog.class, Document.class);
        logger.info(">>> rawResults:{}", aggregationResults.getRawResults());
        logger.info(">>> mappedResults:{}", aggregationResults.getMappedResults());
        //结果 document
        List<Document> mappedResults = aggregationResults.getMappedResults();
        List<CountVO> vos = Lists.newArrayList();
        for (Document doc : mappedResults) {
            vos.add(new CountVO((String) doc.get("_id"), (Integer) doc.get("count")));
        }
        logger.info(">>> size:{}, vos:{}", vos.size(), vos);

        //---------------------- api不支持group复杂写法
        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation
                        .project("buyerNick", "payment", "orders", "num")
                        .andExpression("sendTime")
                        .plus(MongoCriteriaUtil.HOURS_8)
                        .extractMonth()
                        .as("month"),
                Aggregation.match(httpMethodCriteria),
                Aggregation.unwind("orders"),
                Aggregation
                        .group("buyerNick")
                        .first("buyerNick").as("buyerNick")
                        .sum("payment").as("totalPayment")
                        .sum("num").as("itemNum")
                        .count().as("orderNum"),
                Aggregation.sort(Sort.Direction.DESC, "totalPayment"),
                Aggregation.skip(100L),
                Aggregation.limit(10)
        );
        logger.info(">>> aggregation:{}", customerAgg);


        // 2. Bson写法

//        Set<String> onumberSet=new HashSet<>();
//        onumberSet.add("abcd113");
//        onumberSet.add("adxc332");
//        //过滤条件
//        BasicDBObject queryObject=new BasicDBObject("onumber", new BasicDBObject("$in",onumberSet));
//        BasicDBObject queryMatch=new BasicDBObject("$match",queryObject);
//        logger.info(">>> match:{}", queryMatch);
//        //展开数组
//        BasicDBObject queryUnwind=new BasicDBObject("$unwind","$items");
//        //分组统计
//        BasicDBObject groupObject=new BasicDBObject("_id",new BasicDBObject("ino","$items.ino"));
//        groupObject.put("total", new BasicDBObject("$sum","$items.quantity"));
//        BasicDBObject  queryGroup=new BasicDBObject("$group",groupObject);
//        //过滤条件
//        BasicDBObject finalizeMatch=new BasicDBObject("$match",new BasicDBObject("total",new BasicDBObject("$gt",1)));
//
//        List<BasicDBObject> piplelines = Lists.newArrayList(queryMatch,queryUnwind,queryGroup,finalizeMatch);
//        AggregateIterable<Document> results = mongoTemplate.getCollection("orders").aggregate(piplelines);
//        for (Document doc : results) {
//            logger.info(">>> doc:{}", doc);
//        }

        /**
         * db.requestlog.aggregate([
         *         {$match:{
         *             createTime:{
         *                 $gt:ISODate("2019-09-09T16:00:00.000Z"),
         *                 $lt:ISODate("2019-09-30T18:00:00.000Z")}
         *             }
         *         },
         *         {$group:{
         *             _id:{
         *                 methodName:'$methodName'
         *                 month:{$month:{$add:['$createTime',8]}},
         *                 day:{$dayOfMonth:{$add:['$createTime',8]}}},
         *             count:{$sum:1}
         *             }
         *         }
         * ]);
         **/

        Date start = DateFormatUtil.parse("2019-09-25 16:00:00", DateFormatUtil.DEFAULT_DATE_PATTERN);
        Date end = DateFormatUtil.parse("2019-09-30 18:00:00", DateFormatUtil.DEFAULT_DATE_PATTERN);

        BasicDBObject match = MongoBsonQueryUtil
                .and(MongoBsonQueryUtil.gt("createTime", start, false),
                        // new BasicDBObject("createTime", new BasicDBObject("$gt", start))
                        MongoBsonQueryUtil.le("createTime", end, false))
                .append("httpStatus", "200");
        logger.info(">>> match:{}", match);

        BasicDBObject group = new BasicDBObject();
        BasicDBObject _id = new BasicDBObject();
        _id.put("methodName", "$methodName");
        _id.put("month", new BasicDBObject("$month", MongoBsonAggregationUtil.add("createTime", 8)));
        // _id.put("day", new BasicDBObject("$dayOfMonth", new BasicDBObject("$add", new Object[]{"$createTime", 8})));
        _id.put("day", new BasicDBObject("$dayOfMonth", MongoBsonAggregationUtil.add("createTime", 8)));

        group.put("_id", _id);
        group.put("count", new BasicDBObject("$sum", 1));
        logger.info(">>> group:{}", group);

        List<BasicDBObject> pipelines = Lists.newArrayList(
                new BasicDBObject("$match", match),
                new BasicDBObject("$group", group)
        );

        AggregateIterable<Document> docs = mongoTemplate.getCollection("requestlog").aggregate(pipelines);
        for (Document doc : docs) {
            logger.info(">>> doc:{}", doc);
        }

    }

    /**
     * 底层聚合写法
     **/
    @Test
    public void testRawAggregation() {

        BasicDBObject bson = new BasicDBObject();
        bson.put("$eval", "db.requestlog.aggregate([\n" +
                "    {$match:{\n" +
                "        'createTime':{$gt:ISODate(\"2019-09-09T08:00:00.000Z\"),\n" +  // 查询条件需要 -8h 变为0时区
                "                $lt:ISODate(\"2019-09-19T08:59:59.000Z\")},\n" +
//                "        'createTime':{$gt:ISODate(\"2019-05-01T00:00:00.000Z\"),\n" +
//                "                $lt:ISODate(\"2019-12-30T23:59:59.000Z\")},\n" +
                "        }\n" +
                "    },\n" +
                "    {$group:{\n" +
                "            _id:{\n" +
                "                time:'$createTime',\n" + // 返回结果是正确的时区
//                "                time:{$month:{$add:['$createTime',8]}},\n" +
                "                count:'$methodName'},\n" +
                "            count:{$sum:1}\n" +
                "           }\n" +
                "    }\n" +
                "])");

        Document document = mongoTemplate.getDb().runCommand(bson);
        logger.info(">>> {}", document);
        Map<String, Object> retval = (Map<String, Object>) document.get("retval");
        logger.info(">>> retval:{}", retval);
        List<Object> _batch = (List<Object>) retval.get("_batch");
        logger.info(">>> _batch:{}", _batch);

    }

    @Data
    @AllArgsConstructor
    class CountVO {
        private String methodName;
        private Integer count;

    }

}

