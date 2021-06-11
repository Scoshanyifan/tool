package com.kunbu.common.util.biz.rabbitmq.config;

import com.kunbu.common.util.biz.rabbitmq.callback.RabbitmqConfirmCallBack;
import com.kunbu.common.util.biz.rabbitmq.callback.RabbitmqReturnCallBack;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RabbitmqConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitmqConfirmCallBack rabbitmqConfirmCallBack;

    @Autowired
    private RabbitmqReturnCallBack rabbitmqReturnCallBack;

    @PostConstruct
    public void init() {
        /**
         * 如果加上数据转换，会出现json解析json的情况，因为调用方已经转成json了
         */
//        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
//        messageConverter.setDefaultCharset("UTF-8");
//        this.rabbitTemplate.setMessageConverter(messageConverter);

        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);
        //设置回调函数
        rabbitTemplate.setConfirmCallback(rabbitmqConfirmCallBack);
        rabbitTemplate.setReturnCallback(rabbitmqReturnCallBack);
    }

    @Bean
    public Queue queueVehicle() {
        return new Queue(RabbitmqConstant.QUEUE_VEHICLE, true);
    }

    @Bean
    public Queue queueCar() {
        return new Queue(RabbitmqConstant.QUEUE_CAR, true);
    }

    @Bean
    public Queue queueAll() {
        return new Queue(RabbitmqConstant.QUEUE_ALL, true);
    }

    @Bean
    public TopicExchange exchangeBiz() {
        return new TopicExchange(RabbitmqConstant.EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange exchangeTest() {
        return new TopicExchange(RabbitmqConstant.EXCHANGE_TEST, true, false);
    }

    @Bean
    public Binding bindingVehicle() {
        return BindingBuilder.bind(queueVehicle()).to(exchangeBiz()).with(RabbitmqConstant.QUEUE_VEHICLE);
    }

    @Bean
    public Binding bindingCar() {
        return BindingBuilder.bind(queueCar()).to(exchangeBiz()).with(RabbitmqConstant.QUEUE_CAR);
    }

    /**
     * 该队列用于接收全部消息，所以要根据routing_key来监听所有匹配的队列
     * @return
     */
    @Bean
    public Binding bindingAll() {
        return BindingBuilder.bind(queueAll()).to(exchangeBiz()).with(RabbitmqConstant.ROUTING_KEY_ALL);
    }

}
