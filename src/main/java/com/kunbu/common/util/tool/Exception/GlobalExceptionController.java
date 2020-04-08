package com.kunbu.common.util.tool.Exception;

import com.kunbu.common.util.ResultMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-02 16:46
 **/
@RestControllerAdvice
public class GlobalExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionController.class);

    @ExceptionHandler(Exception.class)
    public ResultMap handleException(Exception e) {
        LOGGER.error(">>> handle exception", e);
        return ResultMap.error("系统异常");
    }

}
