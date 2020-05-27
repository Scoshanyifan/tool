package com.kunbu.common.util.basic.test;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-05-21 10:51
 **/
public class BeanUtilTest {

    public static void main(String[] args) {

        Bean bean = new Bean();
        bean.setI(1);
        bean.setInte(23);
        bean.setExt("ext");
        bean.setStr("str");

        BeanDto dto = new BeanDto();
        dto.setI(2222);
        dto.setStr("new str");

        BeanUtils.copyProperties(dto, bean);
        System.out.println(bean);
        System.out.println(dto);
    }

}

@Data
class Bean {

    private int i;
    private Integer inte;
    private String str;
    private String ext;

}

@Data
class BeanDto {

    private int i;
    private Integer inte;
    private String str;

}

