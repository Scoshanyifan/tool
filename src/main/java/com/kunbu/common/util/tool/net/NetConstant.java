package com.kunbu.common.util.tool.net;

/**
 * @author: KunBu
 * @time: 2019/11/21 10:13
 * @description:
 */
public interface NetConstant {

    String HTTP_METHOD_GET                  = "GET";
    String HTTP_METHOD_POST                 = "POST";

    int DEFAULT_CONNECT_TIMEOUT             = 3000;
    int DEFAULT_READ_TIMEOUT                = 3000;


    String NET_RESULT_SUCCESS               = "success";
    String NET_RESULT_CODE                  = "constant";
    String NET_RESULT_DATA                  = "data";
    String NET_RESULT_MSG                   = "msg";

    String CHARSET_UTF8                     = "UTF-8";
}
