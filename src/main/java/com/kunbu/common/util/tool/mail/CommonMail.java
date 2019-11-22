package com.kunbu.common.util.tool.mail;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-11-20 10:39
 **/
@Data
public class CommonMail {

    public static final String ENCODING_UTF8        = "UTF-8";
    public static final String ENCODING_GBK         = "GBK";

    public static final String MIME_TYPE_JPEG       = "image/jpeg";
    public static final String MIME_TYPE_PNG       = "image/png";
    public static final String MIME_TYPE_BMP       = "image/bmp";
    public static final String MIME_TYPE_GIF       = "image/gif";
    public static final String MIME_TYPE_MP4       = "video/mp4";
    public static final String MIME_TYPE_PDF       = "application/pdf";

    /** 发件人 */
    private String from;
    /** 收件人（必填） */
    private String[] tos;
    /** 抄送人 */
    private String[] ccs;
    /** 密送人 */
    private String[] bccs;
    /** 主题（必填） */
    private String subject;
    /** 内容（必填） */
    private String text;
    private boolean isHtml;
    /** 发送时间 */
    private Date sendTime;
    /** 内嵌资源（MultipartFile） */
    private MultipartFile[] inlines1;
    /** 内嵌资源（文件流，需要手动设置mimeType） */
    private MimeFile[] inlines2;
    /** 附件（MultipartFile） */
    private MultipartFile[] attachments1;
    /** 附件（文件流，需要手动设置mimeType） */
    private MimeFile[] attachments2;

    /** 指定编码（比如图片名称如果是中文需要GBK） */
    private String encoding;

    public CommonMail(String[] tos, String subject, String text) {
        this.tos = tos;
        this.subject = subject;
        this.text = text;
    }

    @Override
    public String toString() {
        return "CommonMail{" +
                "from='" + from + '\'' +
                ", tos=" + Arrays.toString(tos) +
                ", ccs=" + Arrays.toString(ccs) +
                ", bccs=" + Arrays.toString(bccs) +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", isHtml=" + isHtml +
                ", sendTime=" + sendTime +
                ", inlines1=" + (inlines1 != null ? inlines1.length : null) +
                ", inlines2=" + (inlines2 != null ? inlines2.length : null) +
                ", attachments1=" + (attachments1 != null ? attachments1.length : null) +
                ", attachments2=" + (attachments2 != null ? attachments2.length : null) +
                ", encoding='" + encoding + '\'' +
                '}';
    }

    @Data
    public class MimeFile {
        private InputStream resource;
        private String name;
        /** MIME type */
        private String type;

        public MimeFile(InputStream resource, String name, String type) {
            this.resource = resource;
            this.name = name;
            this.type = type;
        }

    }

}
