package com.kunbu.common.util.tool.excel.demo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-06-20 14:31
 **/
@Component
public class ExcelService {

    public List<ExcelEntity> getByKeyAndValue(String key, List<String> values) {

        return Lists.newArrayList();
    }

    public Map<String, String> getOrgName2IdMap(List<String> orgNames) {

        return Maps.newHashMap();
    }
}
