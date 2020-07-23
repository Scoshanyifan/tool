package com.kunbu.common.util.web.controller;

import com.kunbu.common.util.tool.file.FileDTO;
import com.kunbu.common.util.tool.file.FileUtil;
import com.kunbu.common.util.tool.http.HttpHeaderUtil;
import com.kunbu.common.util.web.ApiResult;
import com.kunbu.common.util.tool.file.service.FileService;
import com.kunbu.common.util.web.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

}
