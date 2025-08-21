package com.maxinhai.platform.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName：RestTemplateUtils
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 11:45
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Component
public class RestTemplateUtils {

    @Resource
    private RestTemplate restTemplate;

    /**
     * 发送GET请求，返回指定类型的对象
     *
     * @param url          请求URL
     * @param params       请求参数
     * @param responseType 返回数据类型
     * @param <T>          泛型类型
     * @return 响应结果
     */
    public <T> T get(String url, Map<String, Object> params, Class<T> responseType) {
        // 构建带参数的URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params != null && !params.isEmpty()) {
            params.forEach(builder::queryParam);
        }

        return restTemplate.getForObject(builder.toUriString(), responseType);
    }

    /**
     * 发送GET请求，支持复杂类型返回（如List<T>）
     *
     * @param url          请求URL
     * @param params       请求参数
     * @param responseType 参数化类型引用
     * @param <T>          泛型类型
     * @return 响应结果
     */
    public <T> T getForComplexType(String url, Map<String, Object> params, ParameterizedTypeReference<T> responseType) {
        // 构建带参数的URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params != null && !params.isEmpty()) {
            params.forEach(builder::queryParam);
        }

        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<T> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                responseType
        );

        return response.getBody();
    }

    /**
     * 发送POST请求，提交表单数据
     *
     * @param url          请求URL
     * @param params       表单参数
     * @param responseType 返回数据类型
     * @param <T>          泛型类型
     * @return 响应结果
     */
    public <T> T postForm(String url, Map<String, Object> params, Class<T> responseType) {
        // 创建表单参数
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        if (params != null && !params.isEmpty()) {
            params.forEach(formData::add);
        }

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(formData, createHeaders());
        ResponseEntity<T> response = restTemplate.postForEntity(url, entity, responseType);

        return response.getBody();
    }

    /**
     * 发送POST请求，提交JSON数据
     *
     * @param url          请求URL
     * @param requestBody  请求体（会被序列化为JSON）
     * @param responseType 返回数据类型
     * @param <T>          泛型类型
     * @return 响应结果
     */
    public <T> T postJson(String url, Object requestBody, Class<T> responseType) {
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, createJsonHeaders());
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                responseType
        );

        return response.getBody();
    }

    /**
     * 发送POST请求，提交JSON数据，支持复杂类型返回
     *
     * @param url          请求URL
     * @param requestBody  请求体（会被序列化为JSON）
     * @param responseType 参数化类型引用
     * @param <T>          泛型类型
     * @return 响应结果
     */
    public <T> T postJsonForComplexType(String url, Object requestBody, ParameterizedTypeReference<T> responseType) {
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, createJsonHeaders());
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                responseType
        );

        return response.getBody();
    }

    /**
     * 创建默认请求头
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    /**
     * 创建JSON请求头
     */
    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
