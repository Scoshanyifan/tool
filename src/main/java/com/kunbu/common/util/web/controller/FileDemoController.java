package com.kunbu.common.util.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.kunbu.common.util.tool.config.QiniuConfig;
import com.kunbu.common.util.tool.file.FileDTO;
import com.kunbu.common.util.tool.file.FileUtil;
import com.kunbu.common.util.tool.http.HttpHeaderUtil;
import com.kunbu.common.util.tool.qiniu.QiNiuService;
import com.kunbu.common.util.web.ApiResult;
import com.kunbu.common.util.tool.file.service.FileService;
import com.kunbu.common.util.web.ServiceResult;
import com.qiniu.http.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-03 14:09
 **/
@RestController
@RequestMapping("/file")
public class FileDemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDemoController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private QiNiuService qiNiuService;

    @Autowired
    private QiniuConfig qiniuConfig;

    @PostMapping("/upload")
    public ApiResult upload(HttpServletRequest request, @RequestParam MultipartFile file) {

        FileDTO fileDTO = FileUtil.upload(request, file);
        return ApiResult.result(fileService.saveFile(fileDTO));
    }


    @GetMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileId) {

        String httpHeaderRange = request.getHeader(HttpHeaderUtil.HTTP_HEADER_RANGE);

        ServiceResult<FileDTO> serviceResult = fileService.getFile(fileId, httpHeaderRange);
        if (serviceResult.isSuccess()) {
            LOGGER.info(">>> fileDTO:{}", serviceResult.getData());
            FileUtil.download(request, response, serviceResult.getData());
        }
    }

    @PostMapping("/uploadQiNiuJson")
    public ApiResult uploadQiNiuJson(HttpServletRequest request,
                                 @RequestParam String jsonid,
                                 @RequestParam String filename,
                                 @RequestParam String version,
                                 @RequestParam MultipartFile file) {

        try {
            LOGGER.info(">>> {}", file.getContentType());
            Map<String, String> fileRes = qiNiuService.uploadFile(file.getInputStream(), filename, file.getContentType(), true);
            if (fileRes != null) {
                String fileUrl = qiniuConfig.getUrl() + "/" + fileRes.get("key");
                JSONObject json = new JSONObject();
                json.put("url", fileUrl);
                json.put("filename", filename);
                json.put("version", version);
                json.put("jsonid", jsonid);
                String jsonStr = json.toJSONString();
                LOGGER.info(">>> json:{}", json);
                Map<String, String> jsonRes = qiNiuService.uploadFile(jsonStr.getBytes(), jsonid, Client.JsonMime, true);
                if (jsonRes != null) {
                    return ApiResult.success(qiniuConfig.getUrl() + "/" + jsonRes.get("key"));
                }
            }
        } catch (Exception e) {
            LOGGER.error(">>> uploadQiNiuJson error", e);
        }
        return ApiResult.success();
    }

}
