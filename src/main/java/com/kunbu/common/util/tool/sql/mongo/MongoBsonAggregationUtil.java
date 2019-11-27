package com.kunbu.common.util.tool.sql.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

/**
 * @project: bucks
 * @author: kunbu
 * @create: 2019-09-21 11:33
 **/
public class MongoBsonAggregationUtil {

    private static final BasicDBObject EMPTY_DB_OBJECT = new BasicDBObject();

    private static final String DOLLAR = "$";

    /** Pipeline Aggregation Stages */
    // 常用：project, match, limit, skip, unwind, group, sort, bucket


    /** Boolean Operators  */
    // and / or / not

    /**
     * 同query and
     *
     * @param expressions
     **/
    public static BasicDBObject and(BasicDBObject... expressions) {
        return MongoBsonQueryUtil.and(expressions);
    }

    /**
     * 同query or
     *
     * @param expressions
     **/
    public static BasicDBObject or(BasicDBObject... expressions) {
        return MongoBsonQueryUtil.or(expressions);
    }

    /**
     * { $not: [ expression ] } >>> { $not: [ { $gt: [ "$price", 2.33 ] } ] }
     * <p>
     * ps: 注意和Query下的区别：{ "price": { $not: { $gt: 1.99 } } }
     *
     * @param expression
     **/
    public static BasicDBObject not(BasicDBObject expression) {
        if (expression != null) {
            return new BasicDBObject("$not", new Object[]{expression});
        } else {
            return EMPTY_DB_OBJECT;
        }
    }


    /** Set Operators */
    // setEquals / setIntersection / setUnion / setDifference / setIsSubset / anyElementTrue / allElementTrue


    /** Comparison Operators */
    // cmp / eq / gt / gte / lt / lte / ne

    /**
     * { $cmp: [ expression1, expression2 ] } >>> { $project: { compare: { $cmp: [ "$money", 100 ] } } }
     * <p>
     * Compares two values and returns:
     * -1 if the first value is less than the second.
     * 1 if the first value is greater than the second.
     * 0 if the two values are equivalent
     *
     * @param field
     * @param value
     **/
    public static BasicDBObject cmp(String field, Object value) {
        if (field != null && value != null) {
            field = fillUpDollar(field);
            return new BasicDBObject("$cmp", new Object[]{field, value});
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    /**
     * { $eq: [ expression1, expression2 ] } >>> { $eq: [ "$age", 25 ] }
     *
     * @param field
     * @param value
     **/
    public static BasicDBObject eq(String field, Object value) {
        if (field != null && value != null) {
            field = fillUpDollar(field);
            return new BasicDBObject("$eq", new Object[]{field, value});
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    /**
     * { $ne: [ expression1, expression2 ] } >>> { $ne: [ "$age", 25 ] }
     *
     * @param field
     * @param value
     **/
    public static BasicDBObject ne(String field, Object value) {
        if (field != null && value != null) {
            field = fillUpDollar(field);
            return new BasicDBObject("$ne", new Object[]{field, value});
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    /**
     * { $gt: [ expression1, expression2 ] } >>> { $gt: [ "$age", 25 ] }
     *
     * @param field
     * @param value
     * @param appendEquals
     **/
    public static BasicDBObject gt(String field, Object value, boolean appendEquals) {
        if (field != null && value != null) {
            field = fillUpDollar(field);
            if (appendEquals) {
                return new BasicDBObject("$gte", new Object[]{field, value});
            } else {
                return new BasicDBObject("$gt", new Object[]{field, value});
            }
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    /**
     * { $lt: [ expression1, expression2 ] } >>> { $lt: [ "$age", 25 ] }
     *
     * @param field
     * @param value
     * @param appendEquals
     **/
    public static BasicDBObject lt(String field, Object value, boolean appendEquals) {
        if (field != null && value != null) {
            field = fillUpDollar(field);
            if (appendEquals) {
                return new BasicDBObject("$lte", new Object[]{field, value});
            } else {
                return new BasicDBObject("$lt", new Object[]{field, value});
            }
        } else {
            return EMPTY_DB_OBJECT;
        }
    }


    /** Arithmetic Operators */
    // abs / add / ceil / divide / exp / floor / ln / log / log10 / mod / multiply / pow / sqrt / subtract / trunc

    /**
     * { $add: [ expression1, expression2, ... ] } >>> { $add: [ '$createTime' ,8 ] }
     *
     * @param field
     * @param value
     **/
    public static BasicDBObject add(String field, Object value) {
        if (field != null && value != null) {
            field = fillUpDollar(field);
            return new BasicDBObject("$add", new Object[]{field, value});
        } else {
            return EMPTY_DB_OBJECT;
        }
    }


    /** String Operators */
    // concat / indexOfBytes / indexOfCP / split / strcasecmp / strLenBytes / strLenCP / ...

    /**
     * 字符串拼接
     * { $concat: [ expression1, expression2, ... ] } >>> { $concat: [ "$item", " - ", "$description" ] }
     *
     * @param delimiter 分隔符（可为null）
     * @param fields    如果是字段，需要自带$
     **/
    public static BasicDBObject concat(String delimiter, String... fields) {
        if (fields != null && fields.length > 0) {
            if (delimiter == null) {
                return new BasicDBObject("$concat", new Object[]{fields});
            } else {
                BasicDBList dbList = new BasicDBList();
                for (int i = 0; i < fields.length; i++) {
                    dbList.add(fields[i]);
                    if (i != fields.length - 1) {
                        dbList.add(delimiter);
                    }
                }
                return new BasicDBObject("$concat", dbList);
            }
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    /**
     * 字符串拆分
     * { $split: [ string expression, delimiter ] } >>> { $split: [ "June-15-2013", "-" ] }
     *
     * @param field     如果是字段，需要自带$
     * @param delimiter 分隔符
     **/
    public static BasicDBObject split(String field, String delimiter) {
        if (field != null && delimiter != null) {
            return new BasicDBObject("$split", new Object[]{field, delimiter});
        } else {
            return EMPTY_DB_OBJECT;
        }
    }

    /**
     * 字符串截取
     * { $substr: [ string, start, length ] } >>> { $substr: [ "$quarter", 0, 2 ] }
     *
     * @param field
     * @param start
     * @param length
     **/
    public static BasicDBObject substr(String field, int start, int length) {
        if (field != null) {
            return new BasicDBObject("$substr", new Object[]{field, start, length});
        } else {
            return EMPTY_DB_OBJECT;
        }
    }


    /** Text Search Operators */
    // meta


    /** Array Operators */
    //


    /** Variable Operators */
    // map / let


    /** Literal Operators */
    // literal


    /** Date Operators */
    // literal


    /** Conditional Operators */
    // literal


    /** Data Type Operators */
    // literal


    /**
     * Group Accumulator Operators
     */
    // literal


    // -----

    private static String fillUpDollar(String field) {
        if (field.indexOf(DOLLAR) < 0) {
            return DOLLAR + field;
        } else {
            return field;
        }
    }

    public static void main(String[] args) {

        System.out.println(concat("-", "tom", "jack", "lucy"));
    }

}
