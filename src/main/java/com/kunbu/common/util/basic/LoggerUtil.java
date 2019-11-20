package com.kunbu.common.util.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: spring-practice
 * @description: 日志工厂
 * @author: kunbu
 * @create: 2019-08-07 10:57
 **/
public class LoggerUtil {

    private static final String LOG_TYPE_EXECUTOR             = "Executor";
    private static final String LOG_TYPE_METHOD_CONSUME       = "MethodConsume";

    /**
     * 获取线程池任务的日志管理器
     *
     * @param
     * @return
     * @author kunbu
     * @time 2019/8/19 10:01
     **/
    public static Logger getExecutorLogger() {
        return LoggerFactory.getLogger(LOG_TYPE_EXECUTOR);
    }

    /**
     * 获取方法时间统计的日志管理器
     *
     * @param
     * @return
     * @author kunbu
     * @time 2019/8/19 10:01
     **/
    public static Logger getMethodConsumeLogger() {
        return LoggerFactory.getLogger(LOG_TYPE_METHOD_CONSUME);
    }
}
