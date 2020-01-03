package com.kunbu.common.util.basic.test;

import com.alibaba.fastjson.JSONObject;
import com.kunbu.common.util.ResultMap;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-12-07 14:48
 **/
public class JsonTest {

    public static void main(String[] args) {

        Object result = ResultMap.error("xxx");

        JSONObject json = (JSONObject) JSONObject.toJSON(result);

        System.out.println(json + ", " + json.getInteger("code"));
    }
}
