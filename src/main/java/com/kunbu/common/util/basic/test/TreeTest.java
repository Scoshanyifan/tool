package com.kunbu.common.util.basic.test;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-05-22 10:52
 **/
public class TreeTest {

    public static void main(String[] args) {
        List<DepartmentTreeDto> list = Lists.newArrayList(
                new DepartmentTreeDto("5743d", "one", 1, null),
                new DepartmentTreeDto("ff344", "one", 1, null),
                new DepartmentTreeDto("7564u6", "two", 2, "5743d"),
                new DepartmentTreeDto("2141", "three", 3, "7564u6"),
                new DepartmentTreeDto("sdavcfs", "four11", 4, "2141"),
                new DepartmentTreeDto("sac", "five", 5, "sdavcfs"),
                new DepartmentTreeDto("vvd", "three", 3, "7564u6"),
                new DepartmentTreeDto("42142d", "four22", 4, "vvd"),
                new DepartmentTreeDto("214s", "five1", 5, "sdavcfs")
        );

        System.out.println(JSONObject.toJSONString(listAll(list)));
    }

    public static List<DepartmentTreeDto> listAll(List<DepartmentTreeDto> list) {
        if (!CollectionUtils.isEmpty(list)) {
            Map<Integer, List<DepartmentTreeDto>> level2ListMap = list.stream().collect(Collectors.groupingBy(x -> x.getLevel()));
            int maxLevel = level2ListMap.keySet().stream().max(Integer::compareTo).get();

            int topNum = 1;
            for (int i = maxLevel; i > 0; i--) {
                List<DepartmentTreeDto> subs = level2ListMap.get(i);
                List<DepartmentTreeDto> parents = level2ListMap.get(i - 1);
                if (subs == null || parents == null) {
                    continue;
                }
                Map<String, List<DepartmentTreeDto>> parentId2SubMap = subs.stream().collect(Collectors.groupingBy(x -> x.getParentId()));
                for (DepartmentTreeDto p : parents) {
                    List<DepartmentTreeDto> subList = parentId2SubMap.get(p.getId());
                    p.setSubs(subList);
                }
                topNum = i - 1;
            }
            List<DepartmentTreeDto> departmentTrees = level2ListMap.get(topNum);
            if (departmentTrees != null) {
                return departmentTrees;
            }
        }
        return new ArrayList<>();
    }

}

class DepartmentTreeDto {
    private String id;
    private String name;
    private Integer level;
    private String parentId;
    private List<DepartmentTreeDto> subs;

    public DepartmentTreeDto(String id, String name, Integer level, String parentId) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<DepartmentTreeDto> getSubs() {
        return subs;
    }

    public void setSubs(List<DepartmentTreeDto> subs) {
        this.subs = subs;
    }
}