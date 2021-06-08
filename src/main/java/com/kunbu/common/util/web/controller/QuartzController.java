package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.basic.HttpRequestUtil;
import com.kunbu.common.util.basic.IpUtil;
import com.kunbu.common.util.tool.quartz.QuartzSimpleHelper;
import com.kunbu.common.util.tool.quartz.job.PrintJob;
import com.kunbu.common.util.web.ApiResult;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-03-13 15:47
 **/
@RestController
@RequestMapping("/quartz")
public class QuartzController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzController.class);

    @Autowired
    private QuartzSimpleHelper quartzSimpleHelper;

    @GetMapping("/create")
    public ApiResult createTask(@RequestParam String jobGroup, @RequestParam String jobName, HttpServletRequest request) {
        try {
            quartzSimpleHelper.createSimpleJob(jobGroup, jobName, PrintJob.class);
        } catch (SchedulerException e) {
            LOGGER.error(">>> createTask error", e);
        }
        return ApiResult.success();
    }

    @GetMapping("/once")
    public ApiResult onceTask(@RequestParam String jobGroup, @RequestParam String jobName, HttpServletRequest request) {
        try {
            quartzSimpleHelper.executeOnce(jobGroup, jobName);
        } catch (SchedulerException e) {
            LOGGER.error(">>> onceTask error", e);
        }
        return ApiResult.success();
    }

    @GetMapping("/execute")
    public ApiResult executeTask(@RequestParam String jobGroup, @RequestParam String jobName, HttpServletRequest request) {
        try {
            quartzSimpleHelper.executeSimpleJob(jobGroup, jobName, PrintJob.class);
        } catch (SchedulerException e) {
            LOGGER.error(">>> executeTask error", e);
        }
        return ApiResult.success();
    }

    @GetMapping("/schedule")
    public ApiResult scheduleTask(@RequestParam String jobGroup, @RequestParam String jobName, HttpServletRequest request) {
        try {
            quartzSimpleHelper.executeScheduleJob(jobGroup, jobName, PrintJob.class);
        } catch (SchedulerException e) {
            LOGGER.error(">>> executeTask error", e);
        }
        return ApiResult.success();
    }
}
