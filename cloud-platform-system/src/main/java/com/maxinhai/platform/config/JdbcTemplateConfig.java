package com.maxinhai.platform.config;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement // 启用事务管理
public class JdbcTemplateConfig {

    // 1. 配置数据源
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        // 设置数据库连接参数
        dataSource.setServerNames(new String[]{"localhost"}); // 服务器地址
        dataSource.setPortNumbers(new int[]{5432});          // 端口
        dataSource.setDatabaseName("maxinhai");              // 数据库名
        dataSource.setUser("postgres");                      // 用户名
        dataSource.setPassword("MaXinHai!970923");           // 密码

        // 可选：连接池配置
        dataSource.setApplicationName("postgres");           // 应用名称
        dataSource.setConnectTimeout(5);                     // 连接超时(秒)

        return dataSource;
    }

    // 2. 配置JdbcTemplate
    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());

        // 可选：配置模板行为
        jdbcTemplate.setQueryTimeout(30);            // 查询超时(秒)
        jdbcTemplate.setFetchSize(100);              // 每次获取行数
        jdbcTemplate.setMaxRows(1000);               // 最大返回行数
        jdbcTemplate.setSkipResultsProcessing(true); // 跳过结果处理

        return jdbcTemplate;
    }

    // 3. 配置事务管理器
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
