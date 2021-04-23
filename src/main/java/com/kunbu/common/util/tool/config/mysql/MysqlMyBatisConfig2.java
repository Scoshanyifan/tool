package com.kunbu.common.util.tool.config.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 *
 *
 * @author kunbu
 * @date 2021/1/11 13:01
 **/
@Configuration
@MapperScan(basePackages = "com.kunbu.common.util.tool.sql.mybatis.my",sqlSessionFactoryRef = "mybatisSqlSessionFactory")
public class MysqlMyBatisConfig2 {

    @Bean("mybatisDataSource")
    @ConfigurationProperties("spring.datasource.druid.mybatis")
    public DruidDataSource mybatisDataSource() {
        return new DruidDataSource();
    }

    @Bean("mybatisSqlSessionFactory")
    public SqlSessionFactory mybatisSqlSessionFactory(@Qualifier("mybatisDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }

}
