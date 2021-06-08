package com.kunbu.common.util.tool.qiniu;

import com.alibaba.fastjson.JSONObject;
import com.kunbu.common.util.tool.config.QiniuConfig;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kunbu
 * @date 2021/1/5 16:31
 **/
@Component
public class QiNiuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiNiuService.class);

    @Autowired
    private QiniuConfig qiniuConfig;

    private UploadManager getUploadManager() {
        // 华东
        Configuration configuration = new Configuration(Region.region0());
        return new UploadManager(configuration);
    }

    /**
     * 获取唯一上传上传token
     *
     * @return
     */
    public String getUpToken() {
        return getUpToken(qiniuConfig.getBucket(), null);
    }

    /**
     * 同名文件覆盖操作、只能上传指定key的文件可以可通过此方法获取token
     * <p>
     * scope = bucket:key
     *
     * @param bucketName 空间名称
     * @param key        文件key
     **/
    public String getUpToken(String bucketName, String key) {
        //获取授权对象
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(bucketName, key);
        LOGGER.info(">>> bucketName:{}, key:{}, upToken:{}", bucketName, key, upToken);
        return upToken;
    }

    /**
     * @param inputStream
     * @param fileKey     默认是文件hash
     **/
    public Map<String, String> uploadFile(InputStream inputStream, String fileKey, String mime, boolean appendFile) {
        String upToken;
        if (!appendFile) {
            upToken = getUpToken();
        } else {
            upToken = getUpToken(qiniuConfig.getBucket(), fileKey);
        }
        try {
            // 如果token是通过fileKey得到的，上传通过token来识别，覆盖文件
            Response response = getUploadManager().put(inputStream, fileKey, upToken, null, mime);
            DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);
            LOGGER.info(">>> key:{}, hash:{}", putRet.key, putRet.hash);
            Map<String, String> result = new HashMap<>();
            result.put("key", putRet.key);
            result.put("hash", putRet.hash);
            return result;
        } catch (QiniuException ex) {
            Response r = ex.response;
            LOGGER.error(">>> upload error:{}", r.toString());
            return null;
        }
    }

    public Map<String, String> uploadFile(byte[] bytes, String fileKey, String mime, boolean appendFile) {
        String upToken;
        if (!appendFile) {
            upToken = getUpToken();
        } else {
            upToken = getUpToken(qiniuConfig.getBucket(), fileKey);
        }
        try {
            Response response = getUploadManager().put(bytes, fileKey, upToken, null, mime, true);
            DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);
            LOGGER.info(">>> key:{}, hash:{}", putRet.key, putRet.hash);
            Map<String, String> result = new HashMap<>();
            result.put("key", putRet.key);
            result.put("hash", putRet.hash);
            return result;
        } catch (QiniuException ex) {
            Response r = ex.response;
            LOGGER.error(">>> upload error:{}", r.toString());
            return null;
        }
    }

}
