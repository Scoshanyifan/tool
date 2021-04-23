package com.kunbu.common.util.tool.config.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 简单形式的多数据源配置，jpa和quartz
 *
 * @author kunbu
 * @date 2020/12/4 11:32
 *
 **/
@Configuration
public class MysqlJpaConfig {

    private static final String DATASOURCE_NAME_JPA = "jpaDataSource";

    /**
     * 数据源配置的前缀，必须与application.properties中配置的对应数据源的前缀一致
     */
    private static final String BIZ_DATASOURCE_PREFIX = "spring.datasource.druid.jpa";

    private static final String QUARTZ_DATASOURCE_PREFIX = "spring.datasource.druid.quartz";

    @Bean(name = DATASOURCE_NAME_JPA)
    @ConfigurationProperties(prefix = BIZ_DATASOURCE_PREFIX)
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * @QuartzDataSource 注解则是配置Quartz独立数据源的配置
     */
    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = QUARTZ_DATASOURCE_PREFIX)
    public DataSource quartzDataSource(){
        return new DruidDataSource();
    }

}
