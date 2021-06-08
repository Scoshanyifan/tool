package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.basic.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记录 @RequestMapping / @RequestParam / @RequestBody / @RequestHeader
 * <p>
 * https://www.oschina.net/translate/using-the-spring-requestmapping-annotation
 * https://www.cnblogs.com/blogtech/p/11172168.html
 * content-type对照表：http://tool.oschina.net/commons?type=3nDEFAULT_HIGHLIGHT_COLOR
 * <p>
 * ps：关于 GET / POST
 * 用post，参数就不会在url上
 * 如果是get，url也许会超过某些浏览器与服务器对URL的长度限制
 * <p>
 * <p>
 * 最佳实践：
 * 1. GET请求：参数才url上，使用@RequestParam
 * 2. POST请求：如果是json形式，使用@RequestBody，对象实体 或 Map 来接收
 * 如果是表单形式（form-data或 x-www-form-urlencoded），使用@RequestParam
 * 3. 文件：使用form-data，用@RequestParam
 * 4. 不推荐直接进行对象属性赋值（即不用注解）
 *
 * @project: bucks
 * @author: kunbu
 * @create: 2019-09-17 10:13
 **/
@Controller
@ResponseBody
@RequestMapping("/request")
public class RequestController {

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    /**
     * 将 HTTP 请求映射到 MVC 和 REST 控制器的处理方法上
     * <p>
     * 可添加多个限定
     **/
    @RequestMapping(
            // 限定URI
            value = {"/", "/requestMapping"},
            // 限定请求方式
            method = {RequestMethod.GET, RequestMethod.POST},
            // 限定只处理json和xml
            consumes = {"application/json", "application/XML"},
            // 限定生成json响应（如果有@responseBody注解可忽略）
            produces = {"application/json"},
            // 限定只处理特定头的请求
            headers = {},
            // 限定只处理特定参数的请求
            params = {}
    )
    public Map<String, Object> requestMapping(HttpServletRequest request) {

        HttpRequestUtil.printHttpRequest(request, logger);

        Map<String, Object> resultMap = new HashMap<>();
        return resultMap;
    }

    /**
     * 动态获取uri上的参数
     **/
    @RequestMapping(value = "/pathVariable/{id}", method = RequestMethod.GET)
    public Map<String, Object> pathVariable(
            @PathVariable(value = "id", required = false) String id) {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        return resultMap;
    }

    /**
     * 注解 @RequestParam 获取请求参数
     **/
    @RequestMapping(value = "/requestParam", method = RequestMethod.GET)
    public Map<String, Object> requestParam(
            // 设置了默认值，required将自动设为false（测试带参数和不带参数的区别）
            @RequestParam(value = "name", defaultValue = "kunbu") String name,
            // 默认为必传
            @RequestParam(value = "age") Integer age,
            // 默认使用参数名
            @RequestParam Boolean sex,
            // 直接进行对象属性赋值（不推荐使用，容易和@ReuqestBody混淆）
            String address) {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name", name);
        resultMap.put("age", age);
        resultMap.put("sex", sex);
        return resultMap;
    }

    /**
     * 注解 @RequestHeader 获取请求头中的信息
     **/
    @RequestMapping(value = "/requestHeader", method = RequestMethod.GET)
    public Map<String, Object> requestParam(
            @RequestHeader(value = "content-type") String contentType) {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("contentType", contentType);
        return resultMap;
    }

    /**
     * raw格式（只能用 @RequestBody 接收）包括以下：
     * <p>
     * 1. Content-Type: application/json
     * 2. Content-Type: application/xml
     * 3. Content-Type: text/plain
     * ... ...
     * <p>
     * get post 均有效
     **/
    @RequestMapping(value = "/requestBody", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> requestBody(HttpServletRequest request,
                                           //@RequestBody Map<String, Object> params,
                                           @RequestBody Map<String, Object> params) {

        HttpRequestUtil.printHttpRequest(request, logger);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("params", params);
        return resultMap;
    }

    /**
     * url后的请求参数
     * <p>
     * get post 均有效；用 @RequestParam 接收
     **/
    @RequestMapping(value = "/urlParam", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> urlParam(HttpServletRequest request,
                                        @RequestParam(value = "name", required = false) String name) {

        HttpRequestUtil.printHttpRequest(request, logger);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name", name);
        return resultMap;
    }

    /**
     * Content-Type: multipart/form-data
     * <p>
     * 原生表单提交（文件）
     * <p>
     * get post 均有效；用 @RequestParam 接收
     **/
    @RequestMapping(value = "/formData", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> formData(HttpServletRequest request,
                                        @RequestParam(value = "name", required = false) String name,
                                        @RequestParam("file") MultipartFile file,
                                        @RequestParam List<String> pkList,
                                        @RequestParam(required = false) MultipartFile doc) {

        HttpRequestUtil.printHttpRequest(request, logger);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name", name);
        resultMap.put("file", file.getName());
        if (doc != null) {
            resultMap.put("docName", doc.getOriginalFilename());
        }
        return resultMap;
    }

    /**
     * Content-Type: application/x-www-form-urlencoded
     * <p>
     * 表单encType默认的提交数据的格式（带百分号 % 形式的）
     * <p>
     * 只能是post形式；用 @RequestParam 接收
     **/
    @RequestMapping(value = "/xWwwFormUrlencoded", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> xWwwFormUrlencoded(HttpServletRequest request,
                                                  // post 有效
                                                  @RequestParam(value = "x-www-form-urlencoded", required = false) String name) {

        HttpRequestUtil.printHttpRequest(request, logger);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name", name);
        return resultMap;
    }

}