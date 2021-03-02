package com.kunbu.common.util.tool.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Jedis:
 * 在实现上是直接连接的redis server，如果在多线程环境下是非线程安全的，这个时候只有使用连接池，为每个Jedis实例增加物理连接
 *
 * http://spring.hhui.top/spring-blog/2018/11/01/181101-SpringBoot高级篇Redis之Jedis配置/
 *
 *
 * Lettuce:
 * 连接是基于Netty的，连接实例（StatefulRedisConnection）可以在多个线程间并发访问，应为StatefulRedisConnection是线程安全的，
 * 所以一个连接实例（StatefulRedisConnection）就可以满足多线程环境下的并发访问，当然这个也是可伸缩的设计，一个连接实例不够的情况也可以按需增加连接实例。
 * lettuce主要利用netty实现与redis的同步和异步通信。
 *
 **/
@Configuration
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    /**
     * 配置数据源
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisStandaloneConfiguration redisConfiguration() {
        return new RedisStandaloneConfiguration();
    }

    /**
     * 配置lettuce连接池
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    public GenericObjectPoolConfig redisPool() {
        return new GenericObjectPoolConfig<>();
    }

    /**
     * 配置数据源连接工厂
     *
     * 添加@Primary 指定bean的名称，目的是为了创建两个不同名称的LettuceConnectionFactory
     *
     **/
    @Bean("factory")
    @Primary
    public LettuceConnectionFactory factory(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfiguration) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfiguration, clientConfiguration);
    }

    /**
     * 配置redisTemplate
     *
     **/
    @Bean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("factory") RedisConnectionFactory factory) {
        return createRedisTemplate(factory);
    }

    /**
     * 配置泛型为<String, Object>的RedisTemplate
     * 需要替换掉默认的 JdkSerializationRedisSerializer（此序列化会给目标加上类型信息，即开头出现\xac\xed\x00\x05t\x00\x04字样）
     *
     * https://www.cnblogs.com/zeng1994/p/03303c805731afc9aa9c60dbbd32a323.html
     *
     **/
    private RedisTemplate<String,Object> createRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key用String序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        //json对象映射
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // value用jackson序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        // 不加这句，redis配置成功，但是get时，nativeConnection报空指针
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /** 配置多数据源 */

    @Bean
    @ConfigurationProperties(prefix = "spring.redis2")
    public RedisStandaloneConfiguration redisConfiguration2() {
        return new RedisStandaloneConfiguration();
    }

    @Bean("factory2")
    public LettuceConnectionFactory factory2(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfiguration2) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfiguration2, clientConfiguration);
    }

    @Bean("redisTemplate2")
    public RedisTemplate<String, Object> redisTemplate2(@Qualifier("factory2") RedisConnectionFactory factory2) {
        return createRedisTemplate(factory2);
    }


    /**
     * 配置事件监听容器（用于key的过期，设置，删除事件通知）
     *
     * http://redisdoc.com/topic/notification.html
     *
     **/
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

}
