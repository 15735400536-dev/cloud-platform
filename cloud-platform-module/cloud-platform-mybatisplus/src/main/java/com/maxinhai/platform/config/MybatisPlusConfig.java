package com.maxinhai.platform.config;

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
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
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
