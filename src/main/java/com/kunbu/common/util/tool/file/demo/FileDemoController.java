package com.kunbu.common.util.tool.file.demo;

import com.kunbu.common.util.ResultMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-03 14:09
 **/
@RestController
@RequestMapping("/file")
public class FileDemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDemoController.class);

    @PostMapping("/upload")
    public ResultMap upload(HttpServletRequest request,
                            @RequestParam(value = "file") MultipartFile multiFile) {
        return null;
    }

}
