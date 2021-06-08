package com.kunbu.common.util.tool.sql.mysql.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author kunbu
 * @date 2021/3/29 14:37
 **/
public interface JpaSqlRepository extends JpaRepository<JpaBean, Long>, JpaSpecificationExecutor<JpaBean> {

    List<JpaBean> findAllByJpaType(Integer jpaType);

    @Query(value="select * from jpa_bean where id in (:idList) ",nativeQuery = true)
    List<JpaBean> testSelectInList(@Param("idList") List<Long> idList);

    /**
     * TODO 写法错误
     *
     **/
    @Modifying
    @Query(value="update jpa_bean set jpa_name = ?1, if(?2 != null, jpa_type = ?2) where id = ?3 ",nativeQuery = true)
    int testUpdateIf(String jpaName, Integer jpaType, Long id);

}
