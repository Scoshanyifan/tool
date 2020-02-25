package com.kunbu.common.util.tool.download;

import org.springframework.util.StringUtils;

/**
 *
 * @author kunbu
 */
public class MimeTypeUtil {

    public static String getExt(String originalFileName) {
        String ext = null;
        int index = originalFileName.lastIndexOf(".");
        if (index > 0) {
            ext = originalFileName.substring(index + 1);
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
