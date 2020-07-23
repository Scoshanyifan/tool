package com.kunbu.common.util.tool.sql.mysql;

import com.github.pagehelper.Page;
import com.kunbu.common.util.tool.task.mysql.TaskLock;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: KunBu
 * @time: 2020/6/20 15:48
 * @description:
 */
@Component
@Mapper
public interface CommonMapper {

    String SQL = "`id_card`,`city`,`name`,`age`,`addr`, `create_time`";

    @Insert({
            "<script>",
            "insert into citizen (" + SQL + ") values",
            "<foreach collection='list' item='item' separator=',' >",
            "(#{item.idCard},#{item.city},#{item.name},#{item.age},#{item.addr},#{item.createTime})",
            "</foreach>",
            "</script>",
    })
    int batchInsertCitizen(@Param("list") List<Map<String, Object>> list);


}
