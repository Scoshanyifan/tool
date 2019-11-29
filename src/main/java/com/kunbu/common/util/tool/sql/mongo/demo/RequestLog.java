package com.kunbu.common.util.tool.sql.mongo.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * spring-mongo https://my.oschina.net/u/3452433/blog/2999501
 *
 * @program: bucks
 * @description:
 * @author: kunbu
 * @create: 2019-08-26 17:10
 **/
@Document("requestlog") //映射到数据库的一个集合（collection为集合名称）
public class RequestLog implements Serializable {
    /**
     * 标记id字段
     **/
    @Id
    private String id;

    private String className;
    private String methodName;
    private String parameters;
    private String description;

    private String httpMethod;
    private String httpStatus;
    private String ip;
    private String url;
    private String token;
    private String userId;
    private String userAgent;

    private Long costTime;
    /**
     * 创建单字段索引（默认ASCENDING 升序、DESCENDING 降序）
     **/
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "RequestLog{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters='" + parameters + '\'' +
                ", description='" + description + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", httpStatus='" + httpStatus + '\'' +
                ", ip='" + ip + '\'' +
                ", url='" + url + '\'' +
                ", token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", costTime=" + costTime +
                ", createTime=" + createTime +
                '}';
    }
}
