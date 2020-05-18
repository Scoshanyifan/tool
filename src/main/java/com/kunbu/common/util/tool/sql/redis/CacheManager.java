package com.kunbu.common.util.tool.sql.redis;

import java.io.Serializable;

/**
 * 缓存管理类
 *
 * @author kunbu
 * @time 2019/8/29 10:28
 * @return
 **/
public interface CacheManager {

    /**
     * 获取字符串
     *
     * @param key
     * @return
     * @author kunbu
     * @time 2019/8/27 14:24
     **/
    String getString(String key);

    /**
     * 获取对象
     *
     * @param key
     * @return
     * @author kunbu
     * @time 2019/8/27 14:25
     **/
    Object getObject(String key);

    Boolean exists(String key);

    /**
     * 设值
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(String key, Serializable value);

    /**
     * 带过期时间的设值
     *
     * @param key
     * @param value
     * @param expire seconds
     * @return
     */
    boolean set(String key, Serializable value, long expire);

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     * @author kunbu
     * @time 2019/8/27 14:25
     **/
    boolean existKey(String key);

    /**
     * 删除单个key
     *
     * @param key
     * @return
     * @author kunbu
     * @time 2019/8/27 14:25
     **/
    boolean delKey(String key);

    /**
     * 批量删除keys
     *
     * @param keys
     * @return
     * @author kunbu
     * @time 2019/8/27 14:25
     **/
    Long delKeys(String[] keys);


    /**
     * 设置过期时间
     *
     * @param key
     * @param expire
     * @return
     * @author kunbu
     * @time 2019/8/27 14:26
     **/
    boolean expire(String key, long expire);

    /**
     * 获取key的过期时间
     *
     * @param key
     * @return
     * @author kunbu
     * @time 2019/8/27 14:26
     **/
    Long getExpire(String key);

    /**
     * 递增
     *
     * @param key
     * @return
     * @author kunbu
     * @time 2019/8/27 14:40
     **/
    Long incr(String key);

    /**
     * 增加
     *
     * @param key
     * @param delta
     * @return
     * @author kunbu
     * @time 2019/8/27 14:42
     **/
    Long incr(String key, long delta);

    /**
     * 减少
     *
     * @param key
     * @param delta
     * @return
     * @author kunbu
     * @time 2019/8/27 14:42
     **/
    Long decr(String key, long delta);

    //=================== map ======================

    Object hget(String key, String hashKey);

    boolean hput(String key, String hashKey, Object value);

    Long hdel(String key, Object... hashKeys);

    //================== atomic =====================

    String getSet(String key, String value);

    boolean setnx(String key, Long value);

    boolean setnx(String key, String value);

}
