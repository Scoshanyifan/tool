package com.kunbu.common.util.basic.test;

import java.net.URLDecoder;

/**
 * @author kunbu
 * @date 2020/11/3 14:42
 **/
public class DecodeTest {

    public static void main(String[] args) {

        String eml = "18806719815@qq.com[]XJ";
        String email = "1274462659@qq.com%5B%5DXJ";

        String decode = URLDecoder.decode(eml);
        String decode2 = URLDecoder.decode(email);
        System.out.println(decode);
        System.out.println(decode2);
    }

}
