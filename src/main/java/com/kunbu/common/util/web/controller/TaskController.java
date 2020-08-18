package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.tool.task.TaskTypeEnum;
import com.kunbu.common.util.tool.task.mysql.TaskLock;
import com.kunbu.common.util.tool.task.mysql.TaskMysqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-06-20 13:46
 **/
@Component
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskMysqlService taskMysqlService;

    /**
     * 每分钟
     *
     **/
    @Scheduled(cron = "0 * * * * ?")
    @Async
    public void schedule() {
        logger.info("Current Thread : {}", Thread.currentThread().getName());
        logger.info(">>> ====================== task start =======================");
        for (TaskTypeEnum task : TaskTypeEnum.values()) {
            TaskLock taskLock = taskMysqlService.findByCode(task.name());
            logger.info(">>> taskLock:{}", taskLock);
            if (taskLock != null && taskLock.getStatus() == TaskLock.STATUS_FREE) {
                if (taskMysqlService.requireLock(taskLock)) {
                    try {
                        logger.info(">>> task:{} is running", task.name());
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        logger.error(">>> task:{} error, {}", task.name(), e);
                    } finally {
                        if (!taskMysqlService.releaseLock(taskLock)) {
                            logger.error(">>> task:{} release lock failure", task.name());
                        }
                    }
                } else {
                    logger.warn(">>> task:{} require lock failure", task.name());
                }
            }
        }
        logger.info(">>> ====================== task end =======================");
    }


}
