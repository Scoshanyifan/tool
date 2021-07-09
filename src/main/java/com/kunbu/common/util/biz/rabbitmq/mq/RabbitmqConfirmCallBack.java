package com.kunbu.common.util.biz.rabbitmq.mq;

import com.kunbu.common.util.biz.rabbitmq.service.RabbitmqEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ConfirmCallback 用于确认消息是否到达exchange
 */
@Slf4j
@Component
public class RabbitmqConfirmCallBack implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitmqEventService rabbitmqEventService;

    /**
     * 相关数据-correlationData:CorrelationData [id=a1627774-4d5a-4dcd-ab1f-5695350f47d7]
     * 应答-ack:true
     * 原因-cause:null
     *
     * 相关数据-correlationData:null
     * 应答-ack:false
     * 原因-cause:channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'exchange-tes9t' in vhost '/', class-id=60, method-id=40)
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            log.info(">>> rabbitmq confirm\n相关数据-correlationData:{}\n应答-ack:{}\n原因-cause:{}", correlationData, ack, cause);
            if (correlationData != null && correlationData.getId() != null) {
                rabbitmqEventService.updateEventFail(correlationData.getId());
            }
        }
    }
}
