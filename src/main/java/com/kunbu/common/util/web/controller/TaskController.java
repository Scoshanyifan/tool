package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.tool.task.mysql.TaskMysqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-06-20 13:46
 **/
@RestController
@RequestMapping("/task")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskMysqlService taskMysqlService;

    /**
     * 每分钟
     *
     **/
    @Scheduled(cron = "0 * * * *")
    @Async
    public void schedule() {

    }


}
