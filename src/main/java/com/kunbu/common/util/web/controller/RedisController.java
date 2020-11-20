package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.tool.sql.redis.RedisManager;
import com.kunbu.common.util.tool.sql.redis.RedisManager2;
import com.kunbu.common.util.web.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author kunbu
 * @date 2020/11/20 15:56
 **/
@RestController
@RequestMapping("/redis")
public class RedisController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private RedisManager2 redisManager2;

    @GetMapping("/get")
    public ApiResult get(@RequestParam Boolean primary, @RequestParam String key) {
        Object value;
        if (primary) {
            value = redisManager.getObject(key);
        } else {
            value = redisManager2.getObject(key);
        }
        return ApiResult.success(value);
    }

    @GetMapping("/set")
    public ApiResult set(@RequestParam Boolean primary, @RequestParam String key, @RequestParam Object value) {
        if (primary) {
            redisManager.set(key, (Serializable) value, 30);
        } else {
            redisManager2.set(key, (Serializable) value);
        }
        return ApiResult.success();
    }
}