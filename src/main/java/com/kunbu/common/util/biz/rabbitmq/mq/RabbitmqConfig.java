package com.kunbu.common.util.biz.rabbitmq.mq;

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

    /** =========================== 定义队列 =================================*/
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
    public Queue queueTest() {
        return new Queue(RabbitmqConstant.QUEUE_TEST, true);
    }

    /** =========================== 定义交换机 =================================*/
    @Bean
    public TopicExchange exchangeVehicle() {
        return new TopicExchange(RabbitmqConstant.EXCHANGE_VEHICLE, true, false);
    }

    @Bean
    public TopicExchange exchangeTest() {
        return new TopicExchange(RabbitmqConstant.EXCHANGE_TEST, true, false);
    }

    /** =========================== 定义绑定关系 =================================*/
    @Bean
    public Binding bindingVehicle() {
        return BindingBuilder.bind(queueVehicle()).to(exchangeVehicle()).with(RabbitmqConstant.ROUTING_KEY_VEHICLE);
    }

    @Bean
    public Binding bindingCar() {
        return BindingBuilder.bind(queueCar()).to(exchangeVehicle()).with(RabbitmqConstant.ROUTING_KEY_CAR);
    }

    /**
     * 该队列能够接收 key.* 所有消息
     * @return
     */
    @Bean
    public Binding bindingAll() {
        return BindingBuilder.bind(queueAll()).to(exchangeVehicle()).with(RabbitmqConstant.ROUTING_KEY_ALL);
    }

    @Bean
    public Binding bindingTest() {
        return BindingBuilder.bind(queueTest()).to(exchangeTest()).with(RabbitmqConstant.ROUTING_KEY_TEST);
    }
}
