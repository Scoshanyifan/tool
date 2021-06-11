package com.kunbu.common.util.biz.rabbitmq;

import cn.hutool.json.JSONUtil;
import com.kunbu.common.util.biz.rabbitmq.bean.RabbitmqDto;
import com.kunbu.common.util.biz.rabbitmq.config.RabbitmqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitmqConsumeListener {

    @Autowired
    private RabbitmqEventService rabbitmqEventService;

    @RabbitListener(queues = RabbitmqConstant.QUEUE_VEHICLE)
//    @RabbitHandler
    public void handleVehicle(Object object, Message message, Channel channel) {
        log.info(">>> handleVehicle object:{}, message:{}, channel:{}", object, new String(message.getBody()), JSONUtil.toJsonStr(channel));
        if (message.getBody() != null) {
            RabbitmqDto rabbitmqDto = JSONUtil.toBean(new String(message.getBody()), RabbitmqDto.class);
            log.info(">>> dto:{}", rabbitmqDto);

            rabbitmqEventService.deleteEvent(rabbitmqDto.getCorrelationId());
        }
    }

    @RabbitListener(queues = RabbitmqConstant.QUEUE_CAR)
    public void handleCar(Object object, Message message, Channel channel) {
        log.info(">>> handleCar object:{}, message:{}, channel:{}", object, new String(message.getBody()), JSONUtil.toJsonStr(channel));
        if (message.getBody() != null) {
            RabbitmqDto rabbitmqDto = JSONUtil.toBean(new String(message.getBody()), RabbitmqDto.class);
            log.info(">>> dto:{}", rabbitmqDto);

            rabbitmqEventService.deleteEvent(rabbitmqDto.getCorrelationId());
        }
    }

    @RabbitListener(queues = RabbitmqConstant.QUEUE_ALL)
    public void handleAll(Object object, Message message, Channel channel) {
        log.info(">>> handleAll object:{}, message:{}, channel:{}", object, new String(message.getBody()), JSONUtil.toJsonStr(channel));
    }
}
