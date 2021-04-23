package com.kunbu.common.util.tool.sql.mysql.mybatis.my;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kunbu.common.util.tool.sql.mysql.mybatis.MybatisBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface MybatisMapper extends BaseMapper<MybatisBean> {

    @Select("select count(*) from mybatis_bean where mybatis_type = #{type} ")
    Integer countTotal(@Param("type") String type);
}
