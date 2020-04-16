package com.kunbu.common.util.tool.file.service;

import com.kunbu.common.util.tool.file.FileDTO;

/**
 * @author: KunBu
 * @time: 2020/4/16 11:05
 * @description:
 */
public interface FileService {

    String saveFile(FileDTO fileDTO);

    FileDTO getFile(String fileId);

    /**
     * 支持断点续传
     * 
     * @param fileId
     * @param httpHeaderRange 断点续传的首尾位置 bytes=500-999
     * @author kunbu
     * @time 2020/4/16 14:34
     * @return       
     **/
    FileDTO getFile(String fileId, String httpHeaderRange);

}
