package com.maxinhai.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
public class EmqxConfig {

    @Value("${mqtt.username:admin}")
    private String mqttUsername;

    @Value("${mqtt.password:MaXinHai!970923}")
    private String mqttPassword;

    /**
     * 配置带EMQX基础认证的RestTemplate
     */
    @Bean
    public RestTemplate mqttRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // 添加Basic Auth认证拦截器
        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            String auth = mqttUsername + ":" + mqttPassword;
            byte[] encodedAuth = Base64Utils.encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + new String(encodedAuth);
            request.getHeaders().set("Authorization", authHeader);
            return execution.execute(request, body);
        };
        restTemplate.setInterceptors(Collections.singletonList(authInterceptor));
        return restTemplate;
    }

}
