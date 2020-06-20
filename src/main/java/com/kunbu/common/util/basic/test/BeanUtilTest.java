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

        Bean source = new Bean();
        source.setI(1);
        source.setInte(1);
        source.setExt("ext");
        source.setStr("source");

        BeanDto target = new BeanDto();
        target.setI(3);
        target.setStr("target");

        // 如果target原先有内容，字段相同的会被覆盖
        BeanUtils.copyProperties(source, target);
        System.out.println(source);
        System.out.println(target);
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
    private String dto;

}

