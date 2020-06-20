package com.kunbu.common.util.basic.test;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

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
                new DepartmentTreeDto("1", "一级-1", 1, null),
                new DepartmentTreeDto("3", "二级-1", 2, "1"),
                new DepartmentTreeDto("2", "一级-2", 1, null),
                new DepartmentTreeDto("5", "四级-1", 4, "4"),
                new DepartmentTreeDto("4", "三级-1", 3, "3"),
                new DepartmentTreeDto("6", "五级-1", 5, "5"),
                new DepartmentTreeDto("8", "三级-3", 3, "3"),
                new DepartmentTreeDto("12", "二级-2", 2, "2"),
                new DepartmentTreeDto("7", "三级-2", 3, "12"),
                new DepartmentTreeDto("9", "四级-3", 4, "8")
        );

        // 完整树
        List<DepartmentTreeDto> allTree = toTree(list);
        System.out.println("allTree: " + JSONObject.toJSONString(allTree));

        // 部分树（从某个二级部门开始）
        List<DepartmentTreeDto> partTree = Lists.newArrayList();
        getPartTree(partTree, allTree, "4");
        System.out.println("partTree: " + JSONObject.toJSONString(partTree));

        // 部分列表（从某个二级部门开始，按level正序）
        List<DepartmentInfoDto> listPart = Lists.newArrayList();
        getList(listPart, allTree, "4", false);
        System.out.println("listPart: " + JSONObject.toJSONString(listPart));

    }

    /**
     * 生成树
     *
     * @param list
     * @author kunbu
     * @return
     **/
    private static List<DepartmentTreeDto> toTree(List<DepartmentTreeDto> list) {
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

    /**
     * 找到树中某一元素，返回其下子树（包括自己）
     *
     * @param part
     * @param all
     * @param departmentId
     * @return
     **/
    private static void getPartTree(List<DepartmentTreeDto> part, List<DepartmentTreeDto> all, String departmentId) {
        for (DepartmentTreeDto dto : all) {
            System.out.println("dto:" + dto);
            if (dto.getId().equals(departmentId)) {
                part.add(dto);
            } else {
                if (!CollectionUtils.isEmpty(dto.getSubs())) {
                    getPartTree(part, dto.getSubs(), departmentId);
                }
            }
        }
    }

    /**
     * 找到树中某一元素，以列表形式返回其下子级（包括自己）
     *
     * @param list
     * @param tree
     * @param departmentId
     * @param pass 如果找到目标，直接遍历子级
     * @return
     **/
    private static void getList(List<DepartmentInfoDto> list, List<DepartmentTreeDto> tree, String departmentId, boolean pass) {
        System.out.println("list:" + list);
        if (CollectionUtils.isEmpty(tree)) {
            return;
        }
        for (DepartmentTreeDto dto : tree) {
            System.out.println("dto:" + dto);
            if (pass) {
                DepartmentInfoDto ld = new DepartmentInfoDto();
                BeanUtils.copyProperties(dto, ld);
                list.add(ld);
                if (CollectionUtils.isNotEmpty(dto.getSubs())) {
                    getList(list, dto.getSubs(), departmentId, true);
                }
            }
            if (dto.getId().equals(departmentId)) {
                DepartmentInfoDto ld = new DepartmentInfoDto();
                BeanUtils.copyProperties(dto, ld);
                list.add(ld);
                if (CollectionUtils.isNotEmpty(dto.getSubs())) {
                    getList(list, dto.getSubs(), departmentId, true);
                }
            } else {
                if (CollectionUtils.isNotEmpty(dto.getSubs())) {
                    getList(list, dto.getSubs(), departmentId, false);
                }
            }
        }
    }

}

@Getter
@Setter
class DepartmentInfoDto {
    private String id;
    private String name;
    private Integer level;
    private String parentId;

    @Override
    public String toString() {
        return "DepartmentInfoDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}

@Getter
@Setter
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

    @Override
    public String toString() {
        return "DepartmentTreeDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}