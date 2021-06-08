package com.kunbu.common.util.tool.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author kunbu
 * @date 2020/12/4 14:34
 **/
@Component
public class QuartzSimpleHelper {

    private static final Logger log = LoggerFactory.getLogger(QuartzSimpleHelper.class);

    @Autowired
    @Qualifier("quartzScheduler")
    private Scheduler scheduler;

    public void createSimpleJob(String jobGroup, String jobName, Class<? extends Job> jobClass) throws SchedulerException {
        if (scheduler.checkExists(JobKey.jobKey(jobName, jobGroup))) {
            throw new SchedulerException(MessageFormat.format("JobGroup: {0}, JobName: {1} 已存在", jobGroup, jobName));
        }
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                // 单独构建任务需要持久化
                .storeDurably()
                .build();
        scheduler.addJob(jobDetail, true);
    }

    public void executeOnce(String jobGroup, String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException(MessageFormat.format("JobGroup: {0}, JobName: {1} 不存在", jobGroup, jobName));
        }
        scheduler.triggerJob(jobKey);
        log.info("运行一次定时任务成功, JobGroup: {}, JobName: {}", jobGroup, jobName);
    }

    public void executeSimpleJob(String jobGroup, String jobName, Class<? extends Job> jobClass) throws SchedulerException {
        if (scheduler.checkExists(JobKey.jobKey(jobName, jobGroup))) {
            throw new SchedulerException(MessageFormat.format("JobGroup: {0}, JobName: {1} 已存在", jobGroup, jobName));
        }
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .build();
        Date nowDate = new Date();
        nowDate.setTime(nowDate.getTime() + 5000);
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .startAt(nowDate)
                .build();
        Date date = scheduler.scheduleJob(jobDetail, trigger);
        log.info("创建定时任务成功: {}, JobGroup: {}, JobName: {}, JobClass: {}", date, jobGroup, jobName, jobClass);
    }

    public void executeScheduleJob(String jobGroup, String jobName, Class<? extends Job> jobClass) throws SchedulerException {
        if (scheduler.checkExists(JobKey.jobKey(jobName, jobGroup))) {
            throw new SchedulerException(MessageFormat.format("JobGroup: {0}, JobName: {1} 已存在", jobGroup, jobName));
        }

        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .build();
        // 构建定时器，如果只是单次执行不需要`
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .withRepeatCount(5);
        // 构建触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(scheduleBuilder)
                .build();
        // 执行任务
        Date date = scheduler.scheduleJob(jobDetail, trigger);
        log.info("创建定时任务成功: {}, JobGroup: {}, JobName: {}, JobClass: {}", date, jobGroup, jobName, jobClass);
    }
}
