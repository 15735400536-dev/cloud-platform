package com.maxinhai.platform.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.maxinhai.platform.mapper"})
public class MybatisPlusConfig {

    // 注册分页插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 构建分页插件并配置优化项
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 1. 指定数据库方言（推荐，避免自动检测）
        paginationInnerInterceptor.setDbType(DbType.POSTGRE_SQL);
        // 2. 配置分页溢出处理策略（三种可选）
        // - OVERFLOW_PAGE: 超过总页数时返回最后一页数据
        // - DEFAULT: 超过总页数时返回空数据
        // - IGNORE: 忽略溢出，按原请求页数查询（可能返回空）
        paginationInnerInterceptor.setOverflow(true); // 等价于设置为OVERFLOW_PAGE（3.4.0+版本）
        // 3. 设置单页最大条数限制（防止恶意分页）
        paginationInnerInterceptor.setMaxLimit(100L); // 限制单页最多查100条
        // 4. 优化Count查询（3.5.0+版本支持）
        paginationInnerInterceptor.setOptimizeJoin(true); // 优化关联查询的Count SQL（避免COUNT(*)包含关联表）
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    // 注册慢查询拦截器
//    @Bean
//    public SlowSqlInterceptor slowSqlInterceptor() {
//        SlowSqlInterceptor interceptor = new SlowSqlInterceptor();
//        // 配置阈值（也可以在application.yml里配）
//        Properties props = new Properties();
//        props.setProperty("slowThreshold", "500");// 500ms
//        interceptor.setProperties(props);
//        return interceptor;
//    }

}
