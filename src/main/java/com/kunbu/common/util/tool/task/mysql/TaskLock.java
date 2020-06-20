package com.kunbu.common.util.tool.task.mysql;

import java.io.Serializable;
import java.util.Date;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-06-20 15:52
 **/
public class TaskLock implements Serializable {

    public static final int STATUS_FREE          = 0;
    public static final int STATUS_RUNNING       = 1;

    private Integer id;
    private String code;
    private String name;
    private Integer status;
    private Date createTime;
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
