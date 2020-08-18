package com.kunbu.common.util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 如果要放到外置tomcat中，需要继承SpringBootServletInitializer并重写configure方法
 */
@SpringBootApplication
@EnableScheduling
public class ToolApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ToolApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ToolApplication.class, args);
    }

}
