package com.kunbu.common.util.tool.file;

import java.io.Serializable;
import java.util.Map;

public class FileDTO implements Serializable {

    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String contentType;
    /**
     * 文件内容
     */
    private byte[] bytes;
    /**
     * 额外参数
     */
    private Map<String, String> attrs;

    /**
     * 头部信息
     */
    private Map<String, String> headers;
    /**
     * 请求文件返回状态
     */
    private boolean success;


    public FileDTO(String fileName, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
