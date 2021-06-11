package com.kunbu.common.util.biz.rabbitmq.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kunbu.common.util.biz.rabbitmq.bean.RabbitmqEvent;
import com.kunbu.common.util.tool.sql.mysql.mybatis.MybatisBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RabbitmqEventMapper extends BaseMapper<RabbitmqEvent> {

    @Update("update rabbitmq_event set status = 1, count =count + 1 where correlation_id = #{correlationId}")
    Integer updateMqEventFail(@Param("correlationId") String correlationId);
}
