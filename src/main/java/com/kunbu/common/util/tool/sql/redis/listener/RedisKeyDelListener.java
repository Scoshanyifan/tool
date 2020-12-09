package com.kunbu.common.util.tool.sql.redis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

/**
 * @author kunbu
 * @date 2020/11/19 17:40
 **/
@Component
public class RedisKeyDelListener extends KeyspaceEventMessageListener implements ApplicationEventPublisherAware {

    private static final Logger logger = LoggerFactory.getLogger(RedisKeyDelListener.class);

    /**
     * 如果需要指定数据库，可以自定义规则，此处只监听db-1
     **/
    private static final Topic KEYEVENT_DELETE_TOPIC = new PatternTopic("__keyevent@1__:del");

    private ApplicationEventPublisher publisher;

    public RedisKeyDelListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info(">>> key delete, body:【{}】, channel:【{}】, pattern:【{}】",
                new String(message.getBody()), new String(message.getChannel()), new String(pattern));
    }


    /** 照写expired */

    @Override
    protected void doRegister(RedisMessageListenerContainer container) {
        container.addMessageListener(this, KEYEVENT_DELETE_TOPIC);
    }

    @Override
    protected void doHandleMessage(Message message) {
        this.publishEvent(new RedisKeyExpiredEvent(message.getBody()));
    }

    protected void publishEvent(RedisKeyExpiredEvent event) {
        if (this.publisher != null) {
            this.publisher.publishEvent(event);
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

}
