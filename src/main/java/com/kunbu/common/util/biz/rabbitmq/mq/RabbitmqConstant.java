package com.kunbu.common.util.biz.rabbitmq.mq;

public interface RabbitmqConstant {

    String QUEUE_VEHICLE = "queue.vehicle";
    String QUEUE_CAR = "queue.car";
    String QUEUE_ALL = "queue.all";
    String QUEUE_TEST = "queue.test";

    String EXCHANGE_VEHICLE = "exchange-vehicle";
    String EXCHANGE_TEST = "exchange-test";

    String ROUTING_KEY_VEHICLE = "key.vehicle";
    String ROUTING_KEY_CAR = "key.car";
    // * 只能匹配一个单词
    String ROUTING_KEY_ALL = "key.*";
    // # 匹配一个或多个
//    String ROUTING_KEY_ALL = "key.#";

    String ROUTING_KEY_TEST = "key.test";

}
