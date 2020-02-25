package com.kunbu.common.util.tool.download;

import com.kunbu.common.util.tool.excel.ExcelConst;
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

    private static final String MIME_TYPE_DEFAULT       = "application/octet-stream";

    private static final String BROWSER_SAFARI          = "safari";
    private static final String BROWSER_CHROME          = "chrome";

    private static final String CHARSET_UTF8            = "UTF-8";
    private static final String CHARSET_ISO_8859_1      = "ISO-8859-1";

    /**
     * 通过文件路径下载
     *
     * @param request
     * @param response
     * @param filePath
     **/
    public static void downloadFile(
            HttpServletRequest request,
            HttpServletResponse response,
            String filePath) {

        InputStream is = null;
        try {
            // 实体文件先转换成字节数组
            is = new FileInputStream(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int bytesRead;
            while((bytesRead = is.read(buf)) != -1) {
                baos.write(buf, 0, bytesRead);
            }
            byte[] data = baos.toByteArray();
            // 截取文件名
            String originalFileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);

            downloadFile(request, response, data, originalFileName);
        } catch (FileNotFoundException e) {
            LOGGER.error(">>> downloadFile 文件不存在 ", e);
        } catch (IOException e) {
            LOGGER.error(">>> downloadFile 文件写入异常 ", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                LOGGER.error(">>> downloadFile 资源释放异常", e);
            }
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
        String fileExt = "";
        int dotIdx = originalFileName.lastIndexOf(".");
        if (dotIdx > 0) {
            fileExt = originalFileName.substring(dotIdx + 1);
        }
        response.setContentType(MimeTypeUtil.getContentType(fileExt));
        // excel特殊处理
        handleExcel(fileExt, response);
        // 下载
        download(request, response, data, originalFileName);
    }

    /**
     * 下载文件，需指定格式Content-Type
     *
     * @param request
     * @param response
     * @param data
     * @param originalFileName
     */
    private static void download(
            HttpServletRequest request,
            HttpServletResponse response,
            byte[] data,
            String originalFileName) {

        OutputStream out = null;
        try {
            // 文件名编码
            String encodeFileName = encodeFileName(request, originalFileName);
            response.addHeader("Content-Disposition", "attachment;filename=" + encodeFileName);
            // 关闭缓存 Http 1.1 header
            response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
            response.setHeader("Connection", "close");

            out = response.getOutputStream();
            out.write(data);
            out.flush();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(">>> download 编码异常", e);
        } catch (IOException e) {
            LOGGER.error(">>> download 流操作异常", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LOGGER.error(">>> download 资源释放异常", e);
            }
        }
    }

    /**
     * 处理excel的contentType
     *
     * @param fileExt
     * @param response
     * @author kunbu
     * @time 2020/2/25 9:23
     * @return
     **/
    private static void handleExcel(String fileExt, HttpServletResponse response) {
        if (fileExt.indexOf(ExcelConst.EXCEL_XLSX_2007) >= 0) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } else if (fileExt.indexOf(ExcelConst.EXCEL_XLS_2003) >= 0) {
            response.setContentType("application/vnd.ms-excel");
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
        if (userAgent != null && userAgent.contains(BROWSER_SAFARI) && !userAgent.contains(BROWSER_CHROME)) {
            // 先转成utf8的字节数组，然后再转成iso进行传输，最后浏览器会进行iso转码
            byte[] bytes = fileName.getBytes(CHARSET_UTF8);
            return new String(bytes, CHARSET_ISO_8859_1);
        } else {
            return URLEncoder.encode(fileName, CHARSET_UTF8);
        }
    }

}
