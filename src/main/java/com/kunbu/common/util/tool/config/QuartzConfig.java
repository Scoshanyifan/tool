package com.kunbu.common.util.tool.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;

/**
 * @author kunbu
 * @date 2020/12/4 12:35
 **/
@Configuration
public class QuartzConfig {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
//        //获取配置属性
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
//        //在quartz.properties中的属性被读取并注入后再初始化对象
//        propertiesFactoryBean.afterPropertiesSet();
//        //创建SchedulerFactoryBean
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        factory.setcon
//        factory.setQuartzProperties(propertiesFactoryBean.getObject());
//        //使用数据源，自定义数据源
//        factory.setDataSource(this.primaryDataSource);
//        factory.setJobFactory(jobFactory);
//        factory.setWaitForJobsToCompleteOnShutdown(true);//这样当spring关闭时，会等待所有已经启动的quartz job结束后spring才能完全shutdown。
//        factory.setOverwriteExistingJobs(false);
//        factory.setStartupDelay(1);
//        return factory;
//    }

    /**
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean("scheduler")
    public Scheduler getScheduler() {
        return schedulerFactoryBean.getScheduler();
    }

}
