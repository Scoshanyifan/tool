package com.kunbu.common.util.basic.test;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kunbu.common.util.web.ApiResult;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-12-07 14:48
 **/
public class JsonTest {

    public static void testJsonDataType() {
        Object result = ApiResult.success();
        JSONObject json = (JSONObject) JSONObject.toJSON(result);
        System.out.println(json + ", " + json.getInteger("constant"));


        Map<String, Object> map = new HashMap<>();
        map.put("number1", 1.0f);
        map.put("number2", 1.0d);
        map.put("number3", 1f);
        map.put("number4", 1d);
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("map", map);
        System.out.println(jsonMap.toJSONString());


        String mapJsonStr = JSONObject.toJSONString(map);
        System.out.println(mapJsonStr);
        Map<String, Object> mapRes = JSONObject.parseObject(mapJsonStr, Map.class);
        System.out.println(mapRes);
    }

    public static void main(String[] args) {

//        testJsonDataType();


        AttrStructDto structDto = new AttrStructDto();
        structDto.setId(1L);
        structDto.setAttrKey("struct");
        structDto.setAttrName("结构体");
        structDto.setDataType(6);
        structDto.setItems(Lists.newArrayList(
                new AttrDto(11L, 1, "boolean", "布尔", "{\"0\":\"关闭\",\"1\":\"开启\"}"),
                new AttrDto(12L, 2, "integer", "整形", "xiw"),
                new AttrDto(13L, 3, "string", "字符串", "{\"strLen\":2}"),
                new AttrDto(13L, 4, "enum", "枚举", "{\"1\":\"正常\",\"2\":\"低速\",\"3\":\"急速\"}")
        ));

        String dataValue = JSONUtil.toJsonStr(structDto);
        System.out.println("dataValue: " + dataValue);

        AttrStructDto dto = JSONUtil.toBean(dataValue, AttrStructDto.class);
        dto.getItems().forEach(System.out::println);
        System.out.println("dto: " + dto);
    }
}

@Data
class AttrDto {
    private Long id;
    private Integer dataType;
    private String attrKey;
    private String attrName;
    private String dataValue;
    public AttrDto(Long id, Integer dataType, String attrKey, String attrName, String dataValue) {
        this.id = id;
        this.dataType = dataType;
        this.attrKey = attrKey;
        this.attrName = attrName;
        this.dataValue = dataValue;
    }
}

@Data
class AttrStructDto {
    private Long id;
    private Integer dataType;
    private String attrKey;
    private String attrName;
    private List<AttrDto> items;
}
