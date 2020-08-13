package com.kunbu.common.util.tool.sql.mysql;

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

    String SQL = "`id_card`,`city`,`account`,`name`,`age`,`address`,`phone`,`create_time`";
    String SQL2 = "`id_card`,`account`,`detail`";

    @Select({
            "select * from citizen limit #{count}"
    })
    List<Map<String, Object>> selectCtizenList(@Param("count") Integer count);

    @Insert({
            "<script>",
            "insert into citizen (" + SQL + ") values",
            "<foreach collection='list' item='item' separator=',' >",
            "(#{item.idCard},#{item.city},#{item.account},#{item.name},#{item.age},#{item.address},#{item.phone},#{item.createTime})",
            "</foreach>",
            "</script>",
    })
    int batchInsertCitizen(@Param("list") List<Map<String, Object>> list);

    @Insert({
            "<script>",
            "insert into citizen_detail (" + SQL2 + ") values",
            "<foreach collection='list' item='item' separator=',' >",
            "(#{item.idCard},#{item.account},#{item.detail})",
            "</foreach>",
            "</script>",
    })
    int batchInsertCitizenDetail(@Param("list") List<Map<String, Object>> list);
}
