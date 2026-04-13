package com.maxinhai.platform.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class GatewayExceptionConfig {

    @Bean
    @Order(-1)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectMapper objectMapper) {
        return (exchange, ex) -> {
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            // 统一返回结果
            Map<String, Object> result = new LinkedHashMap<>();

            if (ex instanceof ResponseStatusException) {
                ResponseStatusException statusEx = (ResponseStatusException) ex;
                HttpStatus status = statusEx.getStatus();

                // 503 服务不可用
                if (status == HttpStatus.SERVICE_UNAVAILABLE) {
                    result.put("code", 503);
                    result.put("msg", "服务暂时不可用，请稍后重试");

                } else if (status == HttpStatus.NOT_FOUND) {
                    result.put("code", 404);
                    result.put("msg", "访问的接口不存在，请检查地址");
                } else if (status == HttpStatus.GATEWAY_TIMEOUT) {
                    result.put("code", 504);
                    result.put("msg", "服务请求超时");
                } else  {
                    // 其他网关异常
                    result.put("code", status.value());
                    result.put("msg", status.getReasonPhrase());
                }
            } else {
                // 未知异常
                result.put("code", 500);
                result.put("msg", "网关异常：" + ex.getMessage());
            }

            result.put("data", null);
            // 设置HTTP状态码为200，由业务code判断
            response.setStatusCode(HttpStatus.OK);

            try {
                byte[] bytes = objectMapper.writeValueAsBytes(result);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }
        };
    }

}
