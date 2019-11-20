package com.kunbu.common.util.basic;

import org.slf4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * @project: bucks
 * @author: kunbu
 * @create: 2019-09-09 14:58
 **/
public class HttpRequestUtil {

    public static void printHttpRequest(HttpServletRequest request, Logger logger) {

        logger.info(">>> request URL: {}", request.getRequestURL());
        logger.info(">>> request URI: {}", request.getRequestURI());
        logger.info(">>> request remote address: {}", request.getRemoteAddr());
        logger.info(">>> request remote host: {}", request.getRemoteHost());
        logger.info(">>> request remote port: {}", request.getRemotePort());
        logger.info(">>> request local port: {}", request.getLocalPort());
        logger.info(">>> request server port: {}", request.getServerPort());
        logger.info(">>> request method: {}", request.getMethod());
        logger.info(">>> request parameter map: {}", request.getParameterMap());
        logger.info(">>> request context path: {}", request.getContextPath());

        Enumeration<String> headers = request.getHeaderNames();
        if (headers != null) {
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                logger.info(">>> requestHeader {}: {}", header, request.getHeader(header));
            }
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                logger.info(">>> cookie, {}: {}", c.getName(), c.getValue());
            }
        }

        HttpSession httpSession = request.getSession();
        if (httpSession != null) {
            logger.info(">>> httpSession id: {}", httpSession.getId());

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
