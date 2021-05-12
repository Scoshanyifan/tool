package com.kunbu.common.util.tool.sql.mysql.mybatis.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kunbu.common.util.tool.sql.mysql.mybatis.CitizenDetailBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: KunBu
 * @time: 2020/6/20 15:48
 * @description:
 */
@Component
@Mapper
public interface CitizenMapper {

    String SQL = "`id_card`,`city`,`account`,`name`,`age`,`address`,`phone`,`create_time`";
    String SQL2 = "`id_card`,`account`,`detail`";

    @Select({
            "select * from citizen limit #{count}"
    })
    List<Map<String, Object>> selectCitizenList(@Param("count") Integer count);

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


    @Select("select cd.* from citizen c left join citizen_detail cd on c.id_card = cd.id_card where c.age = #{age} ")
    List<CitizenDetailBean> getDetailByAge(@Param("age") Integer age);

    @Insert(value = "insert into citizen (" + SQL + ") " +
            " select #{idCard}, #{city}, #{account}, #{name}, #{age}, #{address}, #{phone}, now() ")
    int insertOne(@Param("idCard")String idCard, @Param("city")String city, @Param("account")String account, @Param("name")String name,
                      @Param("age")int age, @Param("address")String address, @Param("phone")String phone, @Param("date")Date date);

    @Select("select count(*) from citizen where id_card = #{idCard} and account = #{account}")
    int countByCondition(@Param("idCard") String idCard, @Param("account") String account);

    @Delete(value = "delete from citizen where id_card = #{idCard} and account = #{account}")
    int deleteByCondition(@Param("idCard") String idCard, @Param("account") String account);



    @Insert(value = "insert into citizen (" + SQL + ") " +
            " select #{idCard}, #{city}, #{account}, #{name}, #{age}, #{address}, #{phone}, now() from dual WHERE NOT EXISTS( " +
            "  select * from citizen " +
            "  where id_card = #{idCard})")
    int insertByNotExistsNormalField(@Param("idCard")String idCard, @Param("city")String city, @Param("account")String account, @Param("name")String name,
                      @Param("age")int age, @Param("address")String address, @Param("phone")String phone, @Param("date")Date date);

    @Insert(value = "insert into citizen (" + SQL + ") " +
            " select #{idCard}, #{city}, #{account}, #{name}, #{age}, #{address}, #{phone}, now() from dual WHERE NOT EXISTS( " +
            "  select * from citizen " +
            "  where account = #{account})")
    int insertByNotExistsIndexField(@Param("idCard")String idCard, @Param("city")String city, @Param("account")String account, @Param("name")String name,
                      @Param("age")int age, @Param("address")String address, @Param("phone")String phone, @Param("date")Date date);


    @Insert(value = "insert into citizen (" + SQL + ") " +
            " select #{idCard}, #{city}, #{account}, #{name}, #{age}, #{address}, #{phone}, now()  " +
            " where ( select id from citizen where id_card = #{idCard} ) is null")
    int insertByCheckNormalField(@Param("idCard")String idCard, @Param("city")String city, @Param("account")String account, @Param("name")String name,
                      @Param("age")int age, @Param("address")String address, @Param("phone")String phone, @Param("date")Date date);

    @Insert(value = "insert into citizen (" + SQL + ") " +
            " select #{idCard}, #{city}, #{account}, #{name}, #{age}, #{address}, #{phone}, now()  " +
            " where ( select id from citizen where account = #{account}) is null")
    int insertByCheckIndexField(@Param("idCard")String idCard, @Param("city")String city, @Param("account")String account, @Param("name")String name,
                      @Param("age")int age, @Param("address")String address, @Param("phone")String phone, @Param("date")Date date);


    @Insert(value = "insert into citizen (" + SQL + ") " +
            " select #{idCard}, #{city}, #{account}, #{name}, #{age}, #{address}, #{phone}, now()  " +
            " where ( select count(*) from citizen where name = #{name}) <= 83")
    int insertByCheckCount(@Param("idCard")String idCard, @Param("city")String city, @Param("account")String account, @Param("name")String name,
                                @Param("age")int age, @Param("address")String address, @Param("phone")String phone, @Param("date")Date date);

}
