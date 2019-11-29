package com.kunbu.common.util.tool.sql.mongo;

import com.kunbu.common.util.basic.TimeUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;

/**
 * spring-data-mongo Criteria下API工具类
 *
 * @program: bucks
 * @author: kunbu
 * @create: 2019-08-27 17:19
 **/
public class MongoCriteriaUtil {

    /**
     * mongodb保存的是0时区时间，查询取值有可能需要转换
     */
    public static final long HOURS_8 = 28800000L;

    /**
     * 返回指定字段
     *
     * @param query
     * @param fields
     **/
    public static void returnFields(Query query, String... fields) {
        if (query != null && fields != null) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }
    }

    /**
     * 追加条件(and)
     *
     * @param query
     * @param criteria
     **/
    public static void addCriteria(Query query, Criteria criteria) {
        if (query != null && criteria != null) {
            query.addCriteria(criteria);
        }
    }

    /**
     * or
     * <p>
     * 1. 如果是同一个字段的或查询，使用此方法
     * <p>
     * 2. 如果是不同字段的or，需要用andOperator把多个orOperator连接起来
     *
     * @param cs
     * @return
     **/
    public static Criteria or(Criteria... cs) {
        if (cs != null && cs.length > 0) {
            return new Criteria().orOperator(cs);
        } else {
            return new Criteria();
        }
    }

    /**
     * and
     * <p>
     * 如果是同一个字段做and查询，Criteria.where("field").xxx().and("field").xxx() 写法错误，即不能用同一个Criteria接收同一字段
     * <p>
     * 会报错：InvalidMongoDbApiUsageException, you can't add a second 'field' expression specified as 'field
     * <p>
     * 解决：用两个Criteria接收
     *
     * @param cs
     * @return
     **/
    public static Criteria and(Criteria... cs) {
        if (cs != null && cs.length > 0) {
            return new Criteria().andOperator(cs);
        } else {
            return new Criteria();
        }
    }

    /**
     * 字符串模糊查询
     *
     * @param field
     * @param regex
     * @return
     **/
    public static Criteria strRegex(String field, String regex) {
        if (field != null && regex != null) {
            return Criteria.where(field).regex(regex);
        } else {
            return new Criteria();
        }
    }

    /**
     * 字符串精确查询
     *
     * @param field
     * @param value
     * @return
     **/
    public static Criteria strIs(String field, String value) {
        if (field != null && value != null) {
            return Criteria.where(field).is(value);
        } else {
            return new Criteria();
        }
    }

    /**
     * 对象精确查询
     *
     * @param field
     * @param value
     * @return
     **/
    public static Criteria objectIs(String field, Object value) {
        if (field != null && value != null) {
            return Criteria.where(field).is(value);
        } else {
            return new Criteria();
        }
    }

    /**
     * 时间区间过滤（需要加上8小时）
     *
     * @param field
     * @param start
     * @param end
     * @return
     **/
    public static Criteria dateCompare(String field, Date start, Date end, boolean plus8Hours) {
        if (start != null && end != null) {
            if (plus8Hours) {
                return Criteria.where(field).gte(TimeUtil.plusHours(start, 8))
                        .andOperator(Criteria.where(field).lte(TimeUtil.plusHours(end, 8)));
            } else {
                return Criteria.where(field).gte(start)
                        .andOperator(Criteria.where(field).lte(end));
            }
        } else {
            return new Criteria();
        }
    }

    /**
     * long值区间过滤
     *
     * @param name
     * @param min
     * @param max
     * @return
     **/
    public static Criteria longCompare(String name, Long min, Long max) {
        if (min != null && max != null) {
            return Criteria.where(name).gte(min).andOperator(Criteria.where(name).lte(max));
        } else {
            return new Criteria();
        }
    }

    //--------------------------------------


}
