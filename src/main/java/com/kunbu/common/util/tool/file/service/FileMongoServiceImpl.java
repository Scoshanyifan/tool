package com.kunbu.common.util.tool.file.service;

import com.kunbu.common.util.tool.file.FileDTO;
import com.kunbu.common.util.tool.http.HttpHeaderUtil;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-11 09:24
 **/
@Component
public class FileMongoServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileMongoServiceImpl.class);

    private static final String MONGO_ID = "_id";



    @Autowired
    GridFsTemplate gridFsTemplate;

    @Override
    public String saveFile(FileDTO fileDTO) {
        byte[] data = fileDTO.getData();
        if (data != null && data.length > 0) {
            InputStream in = new ByteArrayInputStream(data);
            ObjectId id = gridFsTemplate.store(in, fileDTO.getFileName(), fileDTO.getContentType(), fileDTO.getAttrs());
            return id.toString();
        }
        return null;
    }

    @Override
    public FileDTO getFile(String fileId) {
        return getFile(fileId, null);
    }

    @Override
    public FileDTO getFile(String fileId, String httpHeaderRange) {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where(MONGO_ID).is(fileId)));
        if (file != null) {
            GridFsResource resource = gridFsTemplate.getResource(file);

            FileDTO fileDTO = new FileDTO();
            fileDTO.setFileName(resource.getFilename());
            fileDTO.setContentType(resource.getContentType());

            long fileLength = file.getLength();
            long begin = 0;
            long end = fileLength - 1;
            if (httpHeaderRange != null && httpHeaderRange.length() > 0) {
                long[] beginEnd = HttpHeaderUtil.handleRange(httpHeaderRange, fileLength);
                if (beginEnd != null) {
                    begin = beginEnd[0];
                    end = beginEnd[1];
                    fileDTO.setBreakPoint(true);
                    fileDTO.setBeginEnd(beginEnd);
                }
            }
            long contentLength = end - begin + 1;
            fileDTO.setContentLength(contentLength);

            InputStream is = null;
            try {
                is = resource.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                // 如果是断点续传，从指定位置开始读取文件
                is.skip(begin);
                byte[] buf = new byte[1024];
                int index;
                long remain = contentLength;
                while ((index = is.read(buf)) != -1) {
                    bos.write(buf, 0, index);
                    remain -= index;
                    if (remain <= 0) {
                        break;
                    }
                }
                fileDTO.setData(bos.toByteArray());
                fileDTO.setSuccess(true);
                return fileDTO;
            } catch (IOException e) {
                LOGGER.error(">>> getFile fail", e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        LOGGER.error(">>> getFile close fail", e);
                    }
                }
            }
        }
        return null;
    }


}
