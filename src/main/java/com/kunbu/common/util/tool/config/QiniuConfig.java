package com.kunbu.common.util.tool.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "qiniu")
@Data
public class QiniuConfig {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String url;
}
