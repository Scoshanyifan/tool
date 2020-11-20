package com.kunbu.common.util.tool.sql.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存管理器
 *
 * @author kunbu
 * @time 2019/8/27 13:16
 * @return
 **/
@Component
public class RedisManager2 implements CacheManager {

    /**
     * template封装了opsForXXX操作类，用于支持hash, listRequestLog, string, set, zset
     **/
    @Autowired
    @Qualifier("redisTemplate2")
    private RedisTemplate<String, Object> redisTemplate2;

    @Override
    public String getString(String key) {
        return null;
    }

    @Override
    public Object getObject(String key) {
        if (key != null) {
            return redisTemplate2.opsForValue().get(key);
        } else {
            return null;
        }
    }

    @Override
    public Boolean exists(String key) {
        return null;
    }

    @Override
    public boolean set(String key, Serializable value) {
        redisTemplate2.opsForValue().set(key, value);
        return true;
    }

    @Override
    public boolean set(String key, Serializable value, long expire) {
        return false;
    }

    @Override
    public boolean existKey(String key) {
        return false;
    }

    @Override
    public boolean delKey(String key) {
        return false;
    }

    @Override
    public Long delKeys(String[] keys) {
        return null;
    }

    @Override
    public Set<String> getKeys(String pattern) {
        return null;
    }

    @Override
    public boolean expire(String key, long expire) {
        return false;
    }

    @Override
    public Long getExpire(String key) {
        return null;
    }

    @Override
    public Long incr(String key) {
        return null;
    }

    @Override
    public Long incr(String key, long delta) {
        return null;
    }

    @Override
    public Long decr(String key, long delta) {
        return null;
    }

    @Override
    public Object hget(String key, String hashKey) {
        return null;
    }

    @Override
    public boolean hput(String key, String hashKey, Object value) {
        return false;
    }

    @Override
    public Long hdel(String key, Object... hashKeys) {
        return null;
    }

    @Override
    public String getSet(String key, String value) {
        return null;
    }

    @Override
    public boolean setnx(String key, Long value) {
        return false;
    }

    @Override
    public boolean setnx(String key, String value) {
        return false;
    }
}
