package com.kunbu.common.util.tool.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-06-20 17:26
 **/
@Configuration
@EnableAsync
public class AsyncTaskConfig {

    /**
     *   此处成员变量应该从配置中读取，此处仅为演示目的
     */
    private static final int corePoolSize = 5;
    private static final int maxPoolSize = 10;
    private static final int queueCapacity = 5;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }
}
