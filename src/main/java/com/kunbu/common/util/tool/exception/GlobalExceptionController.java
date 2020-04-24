package com.kunbu.common.util.tool.exception;

import com.kunbu.common.util.web.ApiResult;
import com.kunbu.common.util.web.constant.ApiCodeEnum;
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
    public ApiResult handleException(Exception e) {
        LOGGER.error(">>> global exception", e);
        return ApiResult.fail(ApiCodeEnum.SYS_ERROR);
    }

}
