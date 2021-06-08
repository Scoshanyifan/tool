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
@TableName("mybatis_bean")
public class MybatisBean implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("mybatis_type")
    private String mybatisType;

    @TableField("mybatis_name")
    private String mybatisName;

    @TableField("create_time")
    private Date createTime;

}
