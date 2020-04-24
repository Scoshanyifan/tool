package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.web.ApiResult;
import com.kunbu.common.util.basic.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/log")
public class LogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/print")
    public ApiResult query(@RequestParam(required = false) String content, HttpServletRequest request) {

        LOGGER.info(">>> content:{}", content);
        HttpRequestUtil.printHttpRequest(request, LOGGER);

        return ApiResult.success();
    }

}
