package com.kunbu.common.util.tool.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: bucks
 * @description: http://spring.hhui.top/spring-blog/2018/11/01/181101-SpringBoot高级篇Redis之Jedis配置/
 * @author: kunbu
 * @create: 2019-08-26 17:57
 **/
@Configuration
public class RedisConfig {


    /**
     * 泛型为<String,Object>形式的RedisTemplate
     * https://www.cnblogs.com/zeng1994/p/03303c805731afc9aa9c60dbbd32a323.html
     * <p>
     * 需要替换掉默认的 JdkSerializationRedisSerializer（此序列化会给目标加上类型信息，即开头出现\xac\xed\x00\x05t\x00\x04字样）
     *
     * @param factory
     * @return
     * @author kunbu
     * @time 2019/8/26 18:04
     **/
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //自定义序列化
//        CustomizedStringRedisSerializer customizedStringRedisSerializer = new CustomizedStringRedisSerializer();

        // String序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        //json对象映射
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //json序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

}
