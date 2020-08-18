package com.kunbu.common.util.tool.net.test;

import com.kunbu.common.util.tool.net.NetConstant;
import com.kunbu.common.util.tool.net.NetUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-11-21 10:20
 **/
public class NetTest {

    public static void main(String[] args) {

        System.out.println(NetUtil.doRequestWithDefaultSSL(
                "https://www.baidu.com",
                null,
                "GET",
                0,
                0));

        String url = "http://xiao-common.yunext.com/cloud-lift-base/community/list";

        Map<String, Object> params = new HashMap<>();
        params.put("province", "");
        params.put("orgId", "");
        params.put("pageNum", 1);
        params.put("pageSize", -1);

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "SESSION=ZDllMTlkOTYtNGRjYS00NGNlLWJmOWYtMjcwM2Q2MzdlYTEz");

        Map<String, Object> result = NetUtil.doRequest(
                url, params, headers, NetConstant.HTTP_METHOD_GET, null, null, null);

        if (result.get(NetConstant.NET_RESULT_SUCCESS).equals(true)) {
            System.out.println(result.get(NetConstant.NET_RESULT_DATA));
        } else {
            System.out.println(result);
        }

    }

}
