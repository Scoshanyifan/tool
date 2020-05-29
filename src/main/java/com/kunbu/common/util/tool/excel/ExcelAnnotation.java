package com.kunbu.common.util.tool.excel;

import java.lang.annotation.*;

/**
 * @author: KunBu
 * @time: 2020/5/29 10:41
 * @description:
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAnnotation {

    /**
     * 列标题
     */
    String title();

}
