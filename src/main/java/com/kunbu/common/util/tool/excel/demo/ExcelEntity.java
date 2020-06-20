package com.kunbu.common.util.tool.excel.demo;

import com.kunbu.common.util.tool.excel.common.ExcelAnnotation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2019-11-19 13:48
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelEntity {

    @ExcelAnnotation(title = "小区名称")
    private String orgName;

    @ExcelAnnotation(title = "审核状态")
    private String auditState;

    @ExcelAnnotation(title = "用户类型")
    private String userType;

    @ExcelAnnotation(title = "用户姓名")
    private String userName;

    @ExcelAnnotation(title = "身份证号")
    private String idCardNum;

    @ExcelAnnotation(title = "性别")
    private String sex;

    @ExcelAnnotation(title = "手机号")
    private String userPhone;

    private String userRemark;

    private String orgId;

    private String index;

}
