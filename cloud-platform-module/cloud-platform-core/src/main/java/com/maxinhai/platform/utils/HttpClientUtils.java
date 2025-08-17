package com.maxinhai.platform.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HttpClientUtils {

    @Resource
    private CloseableHttpClient httpClient;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 发送GET请求
     */
    public String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * 发送带参数的GET请求
     */
    public String doGet(String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);

        // 设置请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpGet::addHeader);
        }

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("GET请求异常, url: {}", url, e);
        }

        return null;
    }

    /**
     * 发送POST表单请求
     */
    public String doPostForm(String url, Map<String, String> params) {
        return doPostForm(url, params, null);
    }

    /**
     * 发送带请求头的POST表单请求
     */
    public String doPostForm(String url, Map<String, String> params, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpPost::addHeader);
        }

        // 设置表单参数
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> formParams = new ArrayList<>();
            params.forEach((k, v) -> formParams.add(new BasicNameValuePair(k, v)));
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));
        }

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("POST表单请求异常, url: {}", url, e);
        }

        return null;
    }

    /**
     * 发送POST JSON请求
     */
    public String doPostJson(String url, Object jsonBody) {
        return doPostJson(url, jsonBody, null);
    }

    /**
     * 发送带请求头的POST JSON请求
     */
    public String doPostJson(String url, Object jsonBody, Map<String, String> headers) {
        try {
            String json = objectMapper.writeValueAsString(jsonBody);
            return doPostJson(url, json, headers);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON失败", e);
            return null;
        }
    }

    /**
     * 发送POST JSON请求
     */
    public String doPostJson(String url, String jsonBody) {
        return doPostJson(url, jsonBody, null);
    }

    /**
     * 发送带请求头的POST JSON请求
     */
    public String doPostJson(String url, String jsonBody, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpPost::addHeader);
        }

        // 设置JSON参数
        if (jsonBody != null) {
            httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        }

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("POST JSON请求异常, url: {}", url, e);
        }

        return null;
    }

    /**
     * 发送PUT请求
     */
    public String doPut(String url, String jsonBody) {
        HttpPut httpPut = new HttpPut(url);

        // 设置请求头
        httpPut.addHeader("Content-Type", "application/json;charset=UTF-8");

        // 设置JSON参数
        if (jsonBody != null) {
            httpPut.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        }

        try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("PUT请求异常, url: {}", url, e);
        }

        return null;
    }

    /**
     * 发送DELETE请求
     */
    public String doDelete(String url) {
        HttpDelete httpDelete = new HttpDelete(url);

        try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("DELETE请求异常, url: {}", url, e);
        }

        return null;
    }

}
