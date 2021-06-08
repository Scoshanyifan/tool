package com.kunbu.common.util.tool.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kunbu
 * @date 2020/12/4 14:23
 **/
public class PrintJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(PrintJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info(">>> print time job");

    }
}
