package com.kunbu.common.util.tool.sql.mysql.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kunbu
 * @date 2021/3/29 10:44
 **/
@Data
@TableName("citizen_detail")
public class CitizenDetailBean implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("id_card")
    private String idCard;

    @TableField("account")
    private String account;

    @TableField("detail")
    private String detail;

}
