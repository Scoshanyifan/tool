package com.kunbu.common.util.tool.sql.redis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @see KeyExpirationEventMessageListener 是spring原生提供的，如果需要其他事件监听需要自己写
 *
 * @author kunbu
 * @date 2020/11/19 17:40
 **/
//@Component
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisKeyExpireListener.class);

    /**
     * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
     **/
    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info(">>> key expired, body:【{}】, channel:【{}】, pattern:【{}】",
                new String(message.getBody()), new String(message.getChannel()), new String(pattern));
    }
}
