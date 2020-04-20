package com.kunbu.common.util.tool.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-16 11:08
 **/
public class HttpHeaderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHeaderUtil.class);

    public static final long DEFAULT_CACHE_SECONDS = 7 * 24 * 3600L;

    public static final String HTTP_HEADER_RANGE = "Range";

    public static final String HTTP_HEADER_DELIMITER_MINUS = "-";


    public static boolean needCache(String ext) {
        if (ext == null || ext.length() == 0) {
            return false;
        }
        ext = ext.toLowerCase();
        String[] extArr = {"jpg", "jpeg", "png", "gif", "bmp"};
        for (String e : extArr) {
            if (e.equals(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Range: bytes=0-499 表示第 0-499 字节范围的内容
     * Range: bytes=500-999 表示第 500-999 字节范围的内容
     * Range: bytes=-500 表示最后 500 字节的内容
     * Range: bytes=500- 表示从第 500 字节开始到文件结束部分的内容
     *
     * 暂不支持以下形式
     * Range: bytes=0-0,-1 表示第一个和最后一个字节
     * Range: bytes=500-600,601-999 同时指定几个范围
     *
     * @param range
     * @return
     **/
    public static long[] handleRange(String range, long fileLength) {
        try {
            if (range == null || range.length() <= 0) {
                return null;
            }
            range = range.substring("bytes=".length());
            String[] rangeArr = range.split(HTTP_HEADER_DELIMITER_MINUS);

            long[] beginEnd = new long[]{0, fileLength - 1};
            if (rangeArr.length == 1) {
                // bytes=-500
                if (range.startsWith(HTTP_HEADER_DELIMITER_MINUS)) {
                    beginEnd[0] = fileLength - Long.parseLong(rangeArr[0]);
                } else {
                    beginEnd[0] = Integer.parseInt(rangeArr[0]);
                }
            } else if (rangeArr.length == 2) {
                beginEnd[0] = Long.parseLong(rangeArr[0]);
                beginEnd[1] = Long.parseLong(rangeArr[1]);
            } else {
                return null;
            }
            return beginEnd;
        } catch (Exception e) {
            LOGGER.error(">>> handle range fail", e);
            return null;
        }
    }


}
