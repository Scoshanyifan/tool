package com.kunbu.common.util.tool.file;

import com.kunbu.common.util.tool.http.HttpHeaderUtil;
import com.kunbu.common.util.tool.http.MimeTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件上传，下载工具类
 *
 * @author: kunbu
 * @create: 2019-11-23 14:18
 **/
public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private static final String BROWSER_SAFARI          = "safari";
    private static final String BROWSER_CHROME          = "chrome";

    private static final String CHARSET_UTF8            = "UTF-8";
    private static final String CHARSET_ISO_8859_1      = "ISO-8859-1";

    /**
     * 上传文件
     *
     * @param request
     * @param multipartFile
     * @return
     **/
    public static FileDTO upload(HttpServletRequest request, MultipartFile multipartFile) {
        try {
            if (multipartFile != null) {
                FileDTO fileDTO = FileDTO.of(multipartFile.getOriginalFilename(), multipartFile.getBytes());
                fileDTO.setContentType(multipartFile.getContentType());
            }
        } catch (Exception e) {
            LOGGER.error(">>> of fail", e);
        }
        return null;
    }

    /**
     * 通过文件路径下载
     *
     * @param request
     * @param response
     * @param filePath
     **/
    public static void download(HttpServletRequest request, HttpServletResponse response, String filePath) {
        LOGGER.info(">>> download, filePath:{}", filePath);
        if (filePath == null) {
            return;
        }
        InputStream is = null;
        try {
            // 实体文件先转换成字节数组
            is = new FileInputStream(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int index;
            while((index = is.read(buf)) != -1) {
                bos.write(buf, 0, index);
            }
            byte[] data = bos.toByteArray();
            // 截取文件名
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
            download(request, response, data, fileName);
        } catch (FileNotFoundException e) {
            LOGGER.error(">>> download fail, 文件不存在 ", e);
        } catch (IOException e) {
            LOGGER.error(">>> download fail, 文件写入异常 ", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                LOGGER.error(">>> download fail, 资源释放异常", e);
            }
        }
    }

    /**
     * 通过字节数组下载文件
     *
     * @param request
     * @param response
     * @param data
     * @param fileName
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, byte[] data, String fileName) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setData(data);
        fileDTO.setFileName(fileName);
        fileDTO.setSuccess(true);
        download(request, response, fileDTO);
    }

    /**
     * 下载文件
     *
     * @param request
     * @param response
     * @param fileDTO
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, FileDTO fileDTO) {
        OutputStream out = null;
        try {
            if (fileDTO == null || !fileDTO.isSuccess()) {
                return;
            }
            String fileName = fileDTO.getFileName();
            if (fileName == null) {
                return;
            }

            response.reset();
            // 文件类型
            String fileExt = MimeTypeUtil.getExt(fileName);
            String contentType = fileDTO.getContentType();
            if (contentType == null) {
                contentType = MimeTypeUtil.getContentType(fileExt);
            }
            response.setHeader("Content-Type", contentType);
//            response.setContentType(contentType);
            // 文件名编码
            String encodeFileName = encodeFileName(request, fileName);
            // 文件展示或下载
            setInlineOrAttachment(response, encodeFileName, fileDTO.isInline());
            // 文件缓存
            if (HttpHeaderUtil.needCache(fileExt)) {
                response.setHeader("Cache-Control", "max-age=" + HttpHeaderUtil.DEFAULT_CACHE_SECONDS);
            } else {
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache");
//                response.setHeader("Connection", "close");
            }
            // 文件长度
            Long contentLength = fileDTO.getContentLength();
            if (contentLength != null) {
                contentLength = (long) fileDTO.getData().length;
            }
            System.out.println(fileDTO);
            response.setHeader("Content-Length", contentLength + "");
            // 文件断点续传
            if (fileDTO.isBreakPoint()) {
                long[] beginEnd = fileDTO.getBeginEnd();
                long begin = beginEnd[0];
                long end = beginEnd[1];
                // Content-Range: bytes 0-499/22400
                response.setHeader("Content-Range", "bytes " + begin + "-" + end + "/" + contentLength);
            }
            // 文件输出
            out = response.getOutputStream();
            out.write(fileDTO.getData());
            out.flush();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(">>> download fail, 编码异常", e);
        } catch (IOException e) {
            LOGGER.error(">>> download fail, 流操作异常", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LOGGER.error(">>> download fail, 资源释放异常", e);
            }
        }
    }

    /**
     * 1. 中文encode，否则会显示 %8E%34%1A 这种形式
     * 2. 处理safari的乱码问题
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

    /**
     * 设置文件直接在网页展示还是下载
     *
     * @param response
     * @param fileName
     * @param inline
     **/
    private static void setInlineOrAttachment(HttpServletResponse response, String fileName, boolean inline) {
        if (inline) {
            response.setHeader("Content-Disposition", "inline;filename=" + fileName);
        } else {
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        }
    }

}
