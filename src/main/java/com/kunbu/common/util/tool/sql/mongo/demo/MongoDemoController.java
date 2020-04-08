package com.kunbu.common.util.tool.sql.mongo.demo;

import com.kunbu.common.util.PageResult;
import com.kunbu.common.util.ResultMap;
import com.kunbu.common.util.basic.DateFormatUtil;
import com.kunbu.common.util.tool.sql.mongo.MongoCriteriaUtil;
import com.kunbu.common.util.tool.sql.mongo.demo.entity.Order;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-12-02 10:36
 **/
@RestController
@RequestMapping("/mongo")
public class MongoDemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDemoController.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 1. 常规分页：全表扫描
     *
     * 2. 性能分页：不保准准确性，存在误差
     *      原理：通过前一页最后一条记录作为条件，但是不能跳页，需要带上lastId或lastCreateTime条件
     *
     * ps：分页基于游标
     * https://www.cnblogs.com/woshimrf/p/mongodb-pagenation-performance.html
     *
     **/
    @GetMapping("/query")
    public ResultMap query(      /** 查询条件 */
                                 @RequestParam(required = false) String orderNum,
                                 @RequestParam(required = false) String clientName,
                                 @RequestParam(required = false) String start,
                                 @RequestParam(required = false) String end,
                                 /** 分页信息 */
                                 @RequestParam(required = false) Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 /** 是否使用性能查询（使用性能只有下一页，没有页码跳页） */
                                 @RequestParam(required = false) boolean useEffective,
                                 /** 如果不使用_id，也可以用createTime的时间戳 */
                                 @RequestParam(required = false) String lastId) {

        Query query = new Query();
        // 查询条件
        if (StringUtils.isNotBlank(orderNum)) {
            query.addCriteria(Criteria.where("orderNum").regex(orderNum));
        }
        if (StringUtils.isNotBlank(clientName)) {
            query.addCriteria(Criteria.where("clientName").regex(clientName));
        }
        if (StringUtils.isNoneBlank(start, end)) {
            Date startDate = DateFormatUtil.parse(start, DateFormatUtil.DEFAULT_DATE_PATTERN);
            Date endDate = DateFormatUtil.parse(end, DateFormatUtil.DEFAULT_DATE_PATTERN);
            query.addCriteria(MongoCriteriaUtil.dateCompare(
                    "createTime",
                    startDate,
                    // TODO 如果参数是2019-12-12，则需要手动加上24h，以便查询到下一天的时间点
                    new Date(endDate.getTime() + 24 * 3600 * 1000L),
                    false));
        }

        // 先统计总条数
        long total = mongoTemplate.count(query, Order.class);

        // 因为mongodb的_id是按时间生成的，所以单表的时间排序也可以使用_id
        query.with(Sort.by(Sort.Direction.DESC, "_id"));
        // 开启性能查询
        if (useEffective) {
            // 如果最后一条记录为空，说明是第一页
            if (StringUtils.isBlank(lastId)) {
                query.skip(0).limit(pageSize);
            } else {
                // TODO _id必须使用 new ObjectId(lastId)（不能直接用id）
                query.addCriteria(Criteria.where("_id").lt(new ObjectId(lastId))).limit(pageSize);
            }
        } else {
            // 不走性能可以跳页
            if (pageNum != null && pageNum > 0) {
                query.skip((pageNum - 1) * pageSize).limit(pageSize);
            } else {
                // 如果不传页码，默认只发返回第一页
                query.skip(0).limit(pageSize);
            }
        }

        List<Order> list = mongoTemplate.find(query, Order.class);
        PageResult page = PageResult.init(pageNum, pageSize);
        if (CollectionUtils.isNotEmpty(list)) {
            page.setList(list);
            page.setTotal(total);
            // TODO 7/10=1， 20/10=2， 13/10=2
            page.setPages((long) Math.ceil(total / pageSize));
        }
        return ResultMap.success(page);
    }



}
