package com.kunbu.common.util.tool.http;

/**
 *
 * @author kunbu
 */
public class MimeTypeUtil {

    public static final String DEFAULT_MIME_TYPE    = "application/octet-stream";

    public static final String FILE_EXT_DOT         = ".";

    public static String getExt(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return null;
        }
        String ext = null;
        int index = fileName.lastIndexOf(FILE_EXT_DOT);
        if (index > 0) {
            ext = fileName.substring(index + 1);
        }
        return ext;
    }

    public static String getContentType(String ext) {
        //default content type is application/octet-stream
        if (ext == null || ext.length() == 0) {
            return DEFAULT_MIME_TYPE;
        }
        ext = ext.toLowerCase();
        switch (ext) {
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "jpg":
                return "image/jpeg";
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
                return "image/" + ext;
            case "swf":
                return "application/x-shockwave-flash";
            case "flv":
                return "video/x-flv";
            case "mp3":
                return "audio/mpeg";
            case "mp4":
                return "video/mp4";
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "zip":
                return "application/zip";
            default:
                return DEFAULT_MIME_TYPE;
        }
    }

}
