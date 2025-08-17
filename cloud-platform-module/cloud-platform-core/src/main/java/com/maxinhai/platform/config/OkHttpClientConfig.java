package com.maxinhai.platform.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * OkHttpClient配置类
 */
@Configuration
public class OkHttpClientConfig {

    /**
     * 配置OkHttpClient
     */
    @Bean
    public OkHttpClient okHttpClient() {
        // 连接池配置
        ConnectionPool connectionPool = new ConnectionPool(
                50,  // 最大空闲连接数
                5,                    // 连接保持时间
                TimeUnit.MINUTES
        );

        return new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .connectTimeout(10, TimeUnit.SECONDS)    // 连接超时
                .readTimeout(10, TimeUnit.SECONDS)       // 读取超时
                .writeTimeout(10, TimeUnit.SECONDS)      // 写入超时
                .retryOnConnectionFailure(true)                  // 连接失败时重试
                .build();
    }

}
