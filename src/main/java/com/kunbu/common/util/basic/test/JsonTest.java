package com.kunbu.common.util.basic.test;

import com.alibaba.fastjson.JSONObject;
import com.kunbu.common.util.web.ApiResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-12-07 14:48
 **/
public class JsonTest {

    public static void main(String[] args) {

        Object result = ApiResult.success();
        JSONObject json = (JSONObject) JSONObject.toJSON(result);
        System.out.println(json + ", " + json.getInteger("code"));


        Map<String, Object> map = new HashMap<>();
        map.put("number1", 1.0f);
        map.put("number2", 1.0d);
        map.put("number3", 1f);
        map.put("number4", 1d);
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("map", map);
        System.out.println(jsonMap.toJSONString());


    }
}
