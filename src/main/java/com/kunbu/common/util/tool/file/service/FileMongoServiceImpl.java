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
import java.util.HashMap;
import java.util.Map;

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
            Map<String, String> headers = new HashMap<>();

            GridFsResource resource = gridFsTemplate.getResource(file);
            String fileName = resource.getFilename();
            String contentType = resource.getContentType();
            long fileLength = file.getLength();
            int bufferSize = file.getChunkSize();

            long begin = 0;
            long end = fileLength - 1;
            if (httpHeaderRange != null && httpHeaderRange.length() > 0) {
                long[] beginEnd = HttpHeaderUtil.handleRange(httpHeaderRange, fileLength);
                if (beginEnd != null) {
                    begin = beginEnd[0];
                    end = beginEnd[1];
                    // Content-Range: bytes 0-499/22400
                    headers.put("Content-Range", "bytes " + begin + "-" + end + "/" + fileLength);
                }
            }

            long contentLength = end - begin + 1;
            headers.put("Content-Length", contentLength + "");
            GridFsResource gridFsResource = gridFsTemplate.getResource(file);
            InputStream is = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                is = resource.getInputStream();
                //buff用于存放循环读取的临时数据
                byte[] buff = new byte[bufferSize];
                is.skip(begin);
                int read = -1;
                long remain = contentLength;
                long readSize = Math.min(bufferSize, remain);

                while ((read = is.read(buff, 0, (int) readSize)) != -1) {
                    baos.write(buff, 0, read);
                    remain -= read;
                    if (remain <= 0) {
                        break;
                    }
                    readSize = Math.min(bufferSize, remain);
                }
                FileDTO fileDto = new FileDTO();
                return fileDto;
            } catch (IOException e) {
                LOGGER.error(">>> getFile error", e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        LOGGER.error(">>> getFile close error", e);
                    }
                }
            }
        }
        return null;
    }


}
