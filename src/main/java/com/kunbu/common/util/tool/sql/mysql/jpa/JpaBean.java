package com.kunbu.common.util.tool.sql.mysql.jpa;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author kunbu
 * @date 2021/3/29 10:44
 **/
@Entity
@Table(name="jpa_bean")
public class JpaBean implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NotNull(groups = {JpaBean.Update.class})
    private Long id;

    @Column(name = "jpa_type")
    private Integer jpaType;

    @NotBlank(message = "名称不能为空")
    @Column(name = "jpa_name")
    private String jpaName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time")
    private Date createTime;

    public @interface Update {}
}
