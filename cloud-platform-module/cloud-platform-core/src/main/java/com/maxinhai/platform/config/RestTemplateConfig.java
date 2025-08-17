package com.maxinhai.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 配置RestTemplate并设置超时参数
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory requestFactory) {
        return new RestTemplate(requestFactory);
    }

    /**
     * 配置请求工厂，设置连接超时和读取超时
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时时间：单位为毫秒，这里设置为5秒
        factory.setConnectTimeout(5000);
        // 读取超时时间：单位为毫秒，这里设置为10秒
        factory.setReadTimeout(10000);
        return factory;
    }

}
