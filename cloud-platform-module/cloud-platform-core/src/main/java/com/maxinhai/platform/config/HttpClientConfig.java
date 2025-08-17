package com.maxinhai.platform.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * HttpClient配置类
 */
@Configuration
public class HttpClientConfig {

    /**
     * 配置HttpClient连接池
     */
    @Bean
    public PoolingHttpClientConnectionManager connectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        // 设置最大连接数
        connectionManager.setMaxTotal(200);

        // 设置每个路由的最大连接数
        connectionManager.setDefaultMaxPerRoute(100);

        // 设置连接配置
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setCharset(java.nio.charset.StandardCharsets.UTF_8)
                .build();

        connectionManager.setDefaultConnectionConfig(connectionConfig);

        return connectionManager;
    }

    /**
     * 配置HttpClient
     */
    @Bean
    public HttpClient httpClient(PoolingHttpClientConnectionManager connectionManager) {
        // 请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5)  // 连接超时：5秒
                .setConnectionRequestTimeout(3) // 从连接池获取连接超时：3秒
                .setRedirectsEnabled(true)  // 允许重定向
                .build();

        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictIdleConnections(30, TimeUnit.SECONDS)  // 定时清理空闲连接
                .evictExpiredConnections()                   // 清理过期连接
                .setConnectionTimeToLive(60, TimeUnit.SECONDS)  // 连接存活时间
                .build();
    }

}
