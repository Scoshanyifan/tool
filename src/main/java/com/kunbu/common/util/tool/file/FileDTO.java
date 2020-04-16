package com.kunbu.common.util.tool.file;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author kunbu
 **/
public class FileDTO implements Serializable {

    /**
     * 系统中的文件id
     **/
    private String fileId;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String contentType;
    /**
     * 文件大小
     **/
    private long contentLength;
    /**
     * 文件内容
     */
    private byte[] data;
    /**
     * 额外参数
     */
    private Map<String, String> attrs;
    /**
     * 请求文件返回状态
     */
    private boolean success;
    /**
     * 是否断点续传
     **/
    private boolean breakPoint;
    /**
     * 断点续传的首尾位置
     **/
    private long[] beginEnd;

    public static FileDTO of(String fileName, String contentType, byte[] data) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.fileName = fileName;
        fileDTO.contentType = contentType;
        fileDTO.data = data;
        return fileDTO;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isBreakPoint() {
        return breakPoint;
    }

    public void setBreakPoint(boolean breakPoint) {
        this.breakPoint = breakPoint;
    }

    public long[] getBeginEnd() {
        return beginEnd;
    }

    public void setBeginEnd(long[] beginEnd) {
        this.beginEnd = beginEnd;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", contentLength=" + contentLength +
                ", data=" + data != null ? data.length + "" : 0 +
                ", attrs=" + attrs +
                ", success=" + success +
                ", breakPoint=" + breakPoint +
                ", beginEnd=" + Arrays.toString(beginEnd) +
                '}';
    }
}
