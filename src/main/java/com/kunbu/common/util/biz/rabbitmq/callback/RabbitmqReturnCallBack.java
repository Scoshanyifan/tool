package com.kunbu.common.util.biz.rabbitmq.callback;

import com.kunbu.common.util.biz.rabbitmq.RabbitmqEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.PublisherCallbackChannel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ConfirmCallback 用于确认消息是否到达queue
 */
@Slf4j
@Component
public class RabbitmqReturnCallBack implements RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitmqEventService rabbitmqEventService;

    /**
     * 消息体-message:(Body:'{"messageId":"3a5f556c03a24a4b9e220452805cf677","createTime":"2021-06-11 11:03:15","messageData":"qwer"}' MessageProperties [headers={}, contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, deliveryTag=0])
     * 应答码-replyCode:312
     * 描述-replyText:NO_ROUTE
     * 交换器-exchange:exchange-test
     * 路由键-routing:none-topic
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info(">>> rabbitmq return\n消息体-message:{}\n应答码-replyCode:{}\n描述-replyText:{}\n交换器-exchange:{}\n路由键-routing:{}",
                message, replyCode, replyText, exchange, routingKey);

        String correlationId = (String) message.getMessageProperties().getHeaders().get(PublisherCallbackChannel.RETURNED_MESSAGE_CORRELATION_KEY);
        if (correlationId!= null) {
            rabbitmqEventService.updateEventFail(correlationId);
        }
    }
}
