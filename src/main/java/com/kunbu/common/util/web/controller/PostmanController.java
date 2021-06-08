package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.web.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kunbu
 * @date 2021/1/8 13:49
 **/
@RequestMapping("/postman")
@RestController
public class PostmanController {

    @GetMapping("/first")
    public ApiResult requestFirst() {
        return ApiResult.success("123");
    }

    @GetMapping("/second")
    public ApiResult requestSecond(String token) {
        return ApiResult.success(token);
    }

}
