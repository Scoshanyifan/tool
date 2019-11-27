package com.kunbu.common.util.tool.excel.demo;

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

    private String orgName;
    private String auditState;
    private String userType;
    private String userName;
    private String idCardNum;
    private String sex;
    private String userPhone;
    private String userRemark;

}