package com.maxinhai.platform.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @ClassName：ElasticsearchConfig
 * @Author: XinHai.Ma
 * @Date: 2025/9/3 11:32
 * @Description: ElasticSearch配置类
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.maxinhai.platform.repository")
public class ElasticSearchConfig {

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
