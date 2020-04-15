package com.kunbu.common.util.tool.file.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

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
    public ApiResult upload(HttpServletRequest request,
                            @RequestParam(value = "file") MultipartFile multiFile) {
        String path = request.getRealPath("");
        File file = new File(path + "/" + multiFile.getOriginalFilename());
        String fileId = null;
        try {
            multiFile.transferTo(file);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            FileDto fileDto = new FileDto(file.getName(), multiFile.getContentType(), bos.toByteArray());
            fileId = fileService.upload(fileDto);
        } catch (Exception e) {
            return ApiResult.fail("上传失败");
        } finally {
            if (file != null) {
                file.delete();
            }
        }
        String url = systemConfig.getFileDomian() + "/api/file/read?id=" + fileId;
        return ApiResult.success(url);
    }

}
