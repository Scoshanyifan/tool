package com.kunbu.common.util.mail.raw;

import java.io.InputStream;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-11-20 16:52
 **/
public class MailFileVO {

    private InputStream resource;
    private String name;
    /** MIME type */
    private String type;

    public MailFileVO(InputStream resource, String name, String type) {
        this.resource = resource;
        this.name = name;
        this.type = type;
    }
    public InputStream getResource() {
        return resource;
    }
    public void setResource(InputStream resource) {
        this.resource = resource;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
