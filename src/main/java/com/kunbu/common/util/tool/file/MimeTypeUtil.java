package com.kunbu.common.util.tool.file;

/**
 *
 * @author kunbu
 */
public class MimeTypeUtil {

    public static final String DEFAULT_MIME_TYPE    = "application/octet-stream";

    public static final String FILE_EXT_DOT         = ".";

    public static String getExt(String originalFileName) {
        if (originalFileName == null || originalFileName.length() == 0) {
            return null;
        }
        String ext = null;
        int index = originalFileName.lastIndexOf(FILE_EXT_DOT);
        if (index > 0) {
            ext = originalFileName.substring(index + 1);
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
            default:
                return DEFAULT_MIME_TYPE;
        }
    }

}
