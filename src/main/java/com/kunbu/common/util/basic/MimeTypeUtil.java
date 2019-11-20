package com.kunbu.common.util.basic;

import org.springframework.util.StringUtils;

/**
 * @program:web-parent
 * @description:
 * @create:2019-08-26 11:00
 */
public class MimeTypeUtil {

    public static String getExtention(String filename) {
        String ext = null;
        int index = filename.lastIndexOf(".");
        if (index > 0) {
            ext = filename.substring(index + 1);
        }
        return ext;
    }

    public static String getContentType(String ext) {
        //default content type is application/octet-stream
        if (StringUtils.isEmpty(ext)) {
            return "application/octet-stream";
        }
        ext = ext.toLowerCase();
        String type = "application/octet-stream";
        switch (ext) {
            case "jpg":
                type = "image/jpeg";
                break;
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
                type = "image/" + ext;
                break;
            case "swf":
                type = "application/x-shockwave-flash";
                break;
            case "flv":
                type = "video/x-flv";
                break;
            case "mp3":
                type = "audio/mpeg";
                break;
            case "mp4":
                type = "video/mp4";
                break;
            case "pdf":
                type = "application/pdf";
                break;
            case "doc":
                type = "application/msword";
                break;
            default:
                break;
        }
        return type;
    }

}
