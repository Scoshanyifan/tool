package com.kunbu.common.util.biz.rabbitmq.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kunbu.common.util.biz.rabbitmq.bean.RabbitmqEvent;
import com.kunbu.common.util.biz.rabbitmq.dao.RabbitmqEventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitmqEventService {

    @Autowired
    private RabbitmqEventMapper rabbitmqEventMapper;

    public boolean saveEvent(String jsonData, String id) {
        RabbitmqEvent event = new RabbitmqEvent(jsonData, id);
        int insertRes = rabbitmqEventMapper.insert(event);
        log.info(">>> insertRes:{}", insertRes);
        return insertRes > 0;
    }

    public boolean updateEventFail(String id) {
        int updateRes = rabbitmqEventMapper.updateMqEventFail(id);
        log.info(">>> updateRes:{}", updateRes);
        return updateRes > 0;
    }

    public boolean deleteEvent(String id) {
        LambdaQueryWrapper<RabbitmqEvent> query = new LambdaQueryWrapper<>();
        int deleteRes = rabbitmqEventMapper.delete(
                query.eq(RabbitmqEvent::getCorrelationId, id)
        );
        log.info(">>> deleteRes:{}", deleteRes);
        return deleteRes > 0;
    }
}
