package com.kunbu.common.util.tool.file.service;

import com.kunbu.common.util.tool.file.FileDTO;
import com.kunbu.common.util.web.ServiceResult;

/**
 * @author: KunBu
 * @time: 2020/4/16 11:05
 * @description:
 */
public interface FileService {

    ServiceResult<String> saveFile(FileDTO fileDTO);

    ServiceResult<FileDTO> getFile(String fileId);

    /**
     * 支持断点续传
     * 
     * @param fileId
     * @param httpHeaderRange 断点续传的首尾位置 bytes=500-999
     * @author kunbu
     * @time 2020/4/16 14:34
     * @return       
     **/
    ServiceResult<FileDTO> getFile(String fileId, String httpHeaderRange);

}
