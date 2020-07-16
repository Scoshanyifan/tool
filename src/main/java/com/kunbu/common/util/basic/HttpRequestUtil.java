package com.kunbu.common.util.basic;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

/**
 * @project: bucks
 * @author: kunbu
 * @create: 2019-09-09 14:58
 **/
public class HttpRequestUtil {

    public static void printHttpRequest(HttpServletRequest request, Logger logger) {

        logger.info(">>> getRequestURL: {}", request.getRequestURL());
        logger.info(">>> getRequestURI: {}", request.getRequestURI());
        logger.info(">>> getRemoteAddr: {}", request.getRemoteAddr());
        logger.info(">>> getRemoteHost: {}", request.getRemoteHost());
        logger.info(">>> getRemotePort: {}", request.getRemotePort());
        logger.info(">>> getLocalPort: {}", request.getLocalPort());
        logger.info(">>> getServerPort: {}", request.getServerPort());
        logger.info(">>> getMethod: {}", request.getMethod());
        logger.info(">>> getParameter : {}", request.getParameter("key"));
        logger.info(">>> getParameterValues : {}", request.getParameterValues("key"));
        logger.info(">>> getContextPath: {}", request.getContextPath());

        logger.info("=== param ===");
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap != null && !paramMap.isEmpty()) {
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                logger.info(">>> param key:{}, value:{}", entry.getKey(), JSONObject.toJSON(entry.getValue()));
            }
        }

        logger.info("=== header ===");
        Enumeration<String> headers = request.getHeaderNames();
        if (headers != null) {
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                logger.info(">>> header {}: {}", header, request.getHeader(header));
            }
        }

        logger.info("=== cookie ===");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                logger.info(">>> cookie, {}: {}", c.getName(), c.getValue());
            }
        }

        logger.info("=== session ===");
        HttpSession httpSession = request.getSession();
        if (httpSession != null) {
            logger.info(">>> session id: {}", httpSession.getId());

            Enumeration<String> attributes = httpSession.getAttributeNames();
            if (attributes != null) {
                while (attributes.hasMoreElements()) {
                    String attribute = attributes.nextElement();
                    logger.info(">>> httpSession attribute, {}: {}", attribute, httpSession.getAttribute(attribute));
                }
            }
        }
    }

}
