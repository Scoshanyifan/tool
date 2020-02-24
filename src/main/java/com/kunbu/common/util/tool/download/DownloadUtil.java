package com.kunbu.common.util.tool.download;

import com.alibaba.fastjson.util.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件下载工具类
 *
 * @author: kunbu
 * @create: 2019-11-23 14:18
 **/
public class DownloadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadUtil.class);

    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    /**
     * 通过文件路径下载
     *
     * @param request
     * @param response
     * @param path
     * @param originalFileName
     **/
    public static void downloadFile(
            HttpServletRequest request,
            HttpServletResponse response,
            String path,
            String originalFileName) {

        InputStream is = null;
        try {
            is = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int bytesRead;
            while((bytesRead = is.read(buf)) != -1) {
                baos.write(buf, 0, bytesRead);
            }
            byte[] data = baos.toByteArray();
            downloadFile(request, response, data, originalFileName);
        } catch (FileNotFoundException e) {
            System.err.println(">>> 文件不存在 " + e);
        } catch (IOException e) {
            System.err.println(">>> 文件写入异常 " + e);
        } finally {
            IOUtils.close(is);
        }
    }

    /**
     * 通过字节数组下载文件
     *
     * @param request
     * @param response
     * @param data
     * @param originalFileName
     */
    public static void downloadFile(
            HttpServletRequest request,
            HttpServletResponse response,
            byte[] data,
            String originalFileName) {

        response.reset();
        if (data == null || StringUtils.isBlank(originalFileName)) {
            return;
        }
        // 检查文件后缀
        String fileName;
        String fileExt = null;
        int dotIdx = originalFileName.lastIndexOf(".");
        if (dotIdx > 0) {
            fileName = originalFileName.substring(0, dotIdx);
            fileExt = originalFileName.substring(dotIdx + 1);
        } else {
            fileName = originalFileName;
        }
        response.setContentType(MimeTypeUtil.getContentType(fileExt));
        // excel特殊处理
        checkExcel(fileExt, response);
        // 下载
        download(request, response, data, fileName, fileExt);
    }

    /**
     * 下载文件，需指定格式Content-Type
     *
     * @param request
     * @param response
     * @param data
     * @param fileName
     * @param fileExt
     */
    private static void download(
            HttpServletRequest request,
            HttpServletResponse response,
            byte[] data,
            String fileName,
            String fileExt) {

        OutputStream out = null;
        try {
            // 文件名编码
            String encodeFileName = encodeFileName(request, fileName);
            response.addHeader("Content-Disposition", "attachment;filename=" + encodeFileName + fileExt);
            // 关闭缓存 Http 1.1 header
            response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
            response.setHeader("Connection", "close");

            out = response.getOutputStream();
            out.write(data);
            out.flush();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(">>> 下载文件，编码异常", e);
        } catch (IOException e) {
            LOGGER.error(">>> 下载文件，流操作异常", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LOGGER.error(">>> 下载文件，资源释放异常", e);
            }
        }
    }

    private static void checkExcel(String fileExt, HttpServletResponse response) {
        if (fileExt.indexOf("xlsx") >= 0) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            fileExt = ".xlsx";
        } else if (fileExt.indexOf("xls") >= 0) {
            response.setContentType("application/vnd.ms-excel");
            fileExt = ".xls";
        } else {
            return;
        }
    }

    /**
     * 文件名encode，同时处理safari的乱码问题
     *
     * @param request
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String encodeFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        if (userAgent != null && userAgent.contains("safari") && !userAgent.contains("chrome")) {
            // 先转成utf8的字节数组，然后再转成iso进行传输，最后浏览器会进行iso转码
            byte[] bytes = fileName.getBytes("UTF-8");
            return new String(bytes, "ISO-8859-1");
        } else {
            return URLEncoder.encode(fileName, "UTF-8");
        }
    }

}
