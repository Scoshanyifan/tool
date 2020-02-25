package com.kunbu.common.util.basic;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;

import java.util.concurrent.*;

/**
 * @program: spring-practice
 * @description: 线程池
 * @author: kunbu
 * @create: 2019-08-07 10:51
 **/
public class ExecutorUtil {

    private static final Logger LOGGER = LoggerUtil.getExecutorLogger();

    private static int cpuSize = Runtime.getRuntime().availableProcessors();

    private static ExecutorService pool = new ThreadPoolExecutor(
            cpuSize,
            cpuSize * 2,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(1024),
            new ThreadFactoryBuilder().setNameFormat("kunbu-pool-%d").build(),
            // 拒绝策略，这里是丢弃
            new ThreadPoolExecutor.AbortPolicy()) {

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            if (t instanceof RejectedExecutionException) {
                LOGGER.error(">>> ExecutorUtil reject task, r:{}, t:{}", r.toString(), t.getLocalizedMessage(), t);
            }
        }
    };

    /**
     * 任务Runnable推荐编写toString，以便线程池拒绝时打印
     *
     **/
    public static void commitTask(Runnable runnable) {

        pool.execute(runnable);
    }

    public static Future submitTask(Runnable runnable) {

        return pool.submit(runnable);
    }
}
