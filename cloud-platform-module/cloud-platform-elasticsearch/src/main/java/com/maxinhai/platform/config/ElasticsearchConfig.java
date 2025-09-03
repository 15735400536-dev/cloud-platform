package com.maxinhai.platform.config;

import org.apache.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName：ElasticsearchConfig
 * @Author: XinHai.Ma
 * @Date: 2025/9/3 11:32
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.esdemo.repository")
public class ElasticsearchConfig {

    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")
                        // 如果是集群环境，可以添加多个节点
                        // new HttpHost("localhost", 9201, "http")
                )
        );
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }

}
