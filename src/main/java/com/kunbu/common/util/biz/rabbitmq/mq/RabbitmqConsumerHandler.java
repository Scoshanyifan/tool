package com.kunbu.common.util.biz.rabbitmq.mq;

import cn.hutool.json.JSONUtil;
import com.kunbu.common.util.biz.rabbitmq.bean.RabbitmqDto;
import com.kunbu.common.util.biz.rabbitmq.service.RabbitmqEventService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = RabbitmqConstant.QUEUE_TEST)
public class RabbitmqConsumerHandler {

    @RabbitHandler
    public void handleTest(Object object, Message message, Channel channel) {
        log.info(">>> handleTest object:{}, message:{}, channel:{}", object, new String(message.getBody()), JSONUtil.toJsonStr(channel));
    }

}
