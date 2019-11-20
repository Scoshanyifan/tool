package com.kunbu.common.util.mail.raw;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-11-20 10:22
 **/
@Component
@ConfigurationProperties(prefix = "mail")
public class MailConfigResource implements MailConfig {

    private String host;
    private String userName;
    private String password;
    private Integer port;
    private HashMap<String, String> properties;

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Integer getPort() {
        return null;
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "MailConfigResource{" +
                "host='" + host + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                ", properties=" + properties +
                '}';
    }
}
