package com.kunbu.common.util.tool.sql.mongo;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * 基于spring-data-mongo，Bson原生写法
 * <p>
 * http://www.mongoing.com/docs/reference/operator/query.html
 * <p>
 * 3.0
 *
 * @project: bucks
 * @author: kunbu
 * @create: 2019-09-20 20:32
 **/
public class MongoBsonQueryUtil {

    private static final BasicDBObject EMPTY_DB_OBJECT = new BasicDBObject();


    public static BasicDBObject is(String field, Object value) {
        BasicDBObject is = new BasicDBObject();
        if (field != null && value != null) {
            is.put(field, value);
        }
        return is;
    }

    //------------------------------ projection Operators --------------------------

    public static BasicDBObject project(boolean appendId, String... returnFields) {
        BasicDBObject project = new BasicDBObject();
        if (!appendId) {
            project.put("_id", 0);
        }
        if (returnFields != null) {
            for (String field : returnFields) {
                project.put(field, 1);
            }
        }
        return project;
    }

    // eleMatch / slice / meta



    //------------------------------ Comparison Query Operators --------------------------

    /**
     * equals
     * <p>
     * { field: { $eq: value } } >>> { "age": { $eq: 20 } }
     *
     * @param field
     * @param value
     * @return
     **/
    public static BasicDBObject eq(String field, Object value) {
        BasicDBObject eq = new BasicDBObject();
        if (field != null && value != null) {
            eq.put(field, new BasicDBObject("$eq", value));
        }
        return eq;
    }

    /**
     * not equals
     * <p>
     * {field: {$ne: value} } >>> { "sex": { $ne: "man" } }
     *
     * @param field
     * @param value
     * @return
     **/
    public static BasicDBObject ne(String field, Object value) {
        BasicDBObject ne = new BasicDBObject();
        if (field != null && value != null) {
            ne.put(field, new BasicDBObject("$ne", value));
        }
        return ne;
    }

    /**
     * greater than (equals)
     * <p>
     * { field: { $gt: value } } >>> { "age": { $gt: 20 } }
     * { field: { $gte: value } } >>> { "age": { $gte: 22 } }
     *
     * @param field
     * @param value
     * @param appendEqual
     * @return
     **/
    public static BasicDBObject gt(String field, Object value, boolean appendEqual) {
        BasicDBObject gt = new BasicDBObject();
        if (field != null && value != null) {
            if (appendEqual) {
                gt.put(field, new BasicDBObject("$gte", value));
            } else {
                gt.put(field, new BasicDBObject("$gt", value));
            }
        }
        return gt;
    }

    /**
     * less than (equals)
     * <p>
     * { field: { $lt: value } } >>> { "age": { $lt: 20 } }
     * { field: { $lte: value } } >>> { "age": { $lte: 22 } }
     *
     * @param field
     * @param value
     * @param appendEqual
     * @return
     **/
    public static BasicDBObject lt(String field, Object value, boolean appendEqual) {
        BasicDBObject le = new BasicDBObject();
        if (field != null && value != null) {
            if (appendEqual) {
                le.put(field, new BasicDBObject("$lte", value));
            } else {
                le.put(field, new BasicDBObject("$lt", value));
            }
        }
        return le;
    }

    /**
     * { field: { $in: [ value1, value2, ... valueN ] } } >>> { "tags": { $in: ["office", "school"] } }
     *
     * @param field
     * @param value
     * @return
     **/
    public static BasicDBObject in(String field, Object... value) {
        BasicDBObject in = new BasicDBObject();
        if (field != null && value != null) {
            in.put(field, new BasicDBObject("$in", value));
        }
        return in;
    }

    /**
     * not in
     * <p>
     * { field: { $nin: [ value1, value2, ... valueN ] } } >>> { "tags": { $nin: ["office", "school"] } }
     *
     * @param field
     * @param value
     * @return
     **/
    public static BasicDBObject nin(String field, Object... value) {
        BasicDBObject nin = new BasicDBObject();
        if (field != null && value != null) {
            nin.put(field, new BasicDBObject("$nin", value));
        }
        return nin;
    }


    //------------------------------ Logical Query Operators --------------------------

    /**
     * { $and: [ expression1, expression2, ... , expressionN ] }
     * >>>
     * { $and: [ { "price": { $ne: 1.99 } }, { "price": { $exists: true } } ] }
     **/
    public static BasicDBObject and(BasicDBObject... expressions) {
        if (expressions != null && expressions.length > 0) {
            return new BasicDBObject("$and", expressions);
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    /**
     * { $or: [ expression1, expression2, ... , expressionN ] }
     * >>>
     * { $or: [ { "quantity": { $lt: 20 } }, { "price": 10 } ] }
     **/
    public static BasicDBObject or(BasicDBObject... expressions) {
        if (expressions != null && expressions.length > 0) {
            return new BasicDBObject("$or", expressions);
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    /**
     * { $nor: [ expression1, expression2, ... }
     * >>>
     * { $nor: [ { "price": 1.99 }, { "qty": { $lt: 20 } }, { "sale": true } ] }
     **/
    public static BasicDBObject nor(BasicDBObject... expressions) {
        if (expressions != null && expressions.length > 0) {
            return new BasicDBObject("$nor", expressions);
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    /**
     * 不推荐使用（会有歧义）：{ field: { $not: expression } }
     * http://www.mongoing.com/docs/reference/operator/query/not.html
     *
     * eg：{ "price": { $not: { $gt: 1.99 } } } 含义如下
     * 1. the price field value is less than or equal to 1.99 or  #不大于1.99
     * 2. the price field does not exist                          #price字段不存在
     * <p>
     * ps：像$lt, $ne等属于比较运算，而$not属于范围运算
     * <p>
     * <p>
     * db.getCollection('orders').find({$not:{'date':{$gt:'2015-07-02'}}})
     * 报错：unknown top level operator: $not
     *
     * @param expressionWithoutField 没有字段形式的表达式，eg：{ $gt: 1.99 }
     **/
    @Deprecated
    public static BasicDBObject not(String field, BasicDBObject expressionWithoutField) {
        if (field != null && expressionWithoutField != null) {
            return new BasicDBObject(field, new BasicDBObject("$not", expressionWithoutField));
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    //-------------------------- Element Operators -------------------------

    /**
     * { field: { $exists: boolean } } >>> { "name": { $exists: false } }
     * <p>
     * 查询字段是否存在
     * { "_id" : 900, "name" : null }, { "_id" : 901, "name" : "kunbu" }, { "_id" : 902 }  结果是：{ "_id" : 902 }
     *
     * @param field
     * @param exists
     **/
    public static BasicDBObject exists(String field, boolean exists) {
        if (field != null) {
            return new BasicDBObject(field, new BasicDBObject("$exists", exists));
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    /**
     * 类型筛查 http://www.mongoing.com/docs/reference/operator/query/type.html
     *
     * @param field
     * @param number
     **/
    public static BasicDBObject type(String field, int number) {
        // TODO
        return null;
    }

    //--------------------------- Evaluation  Operators -----------------------

    // $mod, $regex, $text, $where

    // -------------------------- Array Operator  ----------------------

    // $all, $elemMatch, $size





    public static void main(String[] args) {
        BasicDBObject is = is("sex", "man");
        BasicDBObject eq = eq("age", 18);
        BasicDBObject ne = ne("age", 21);
        BasicDBObject gt = gt("age", 23, true);
        BasicDBObject lte = lt("age", 45, false);
        BasicDBObject in = in("name", "Tom", "Jack");
        BasicDBObject nin = nin("address", "room", "school");

        List<Bson> bsonList = Lists.newArrayList(is, eq, ne, gt, lte, in, nin);
        bsonList.stream().forEach(System.out::println);

        System.out.println(and(eq, is));
        System.out.println(or(gt, is));
        System.out.println(nor(gt, is));
        System.out.println(not("price", is));
        System.out.println(exists("price", true));

    }
}
