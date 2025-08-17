package com.maxinhai.platform.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class OkHttpClientUtils {

    @Resource
    private OkHttpClient okHttpClient;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 发送GET请求
     */
    public String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * 发送带请求头的GET请求
     */
    public String doGet(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);

        // 设置请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }

        Request request = builder.build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error("GET请求异常, url: {}", url, e);
        }

        return null;
    }

    /**
     * 发送异步GET请求
     */
    public void doGetAsync(String url, Callback callback) {
        doGetAsync(url, null, callback);
    }

    /**
     * 发送带请求头的异步GET请求
     */
    public void doGetAsync(String url, Map<String, String> headers, Callback callback) {
        Request.Builder builder = new Request.Builder().url(url);

        // 设置请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }

        Request request = builder.build();

        okHttpClient.newCall(request).enqueue(callback);
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
        FormBody.Builder formBuilder = new FormBody.Builder();

        // 设置表单参数
        if (params != null && !params.isEmpty()) {
            params.forEach(formBuilder::add);
        }

        Request.Builder builder = new Request.Builder().url(url).post(formBuilder.build());

        // 设置请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }

        Request request = builder.build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
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
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody, mediaType);

        Request.Builder builder = new Request.Builder().url(url).post(body);

        // 设置请求头
        builder.addHeader("Content-Type", "application/json;charset=UTF-8");
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }

        Request request = builder.build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error("POST JSON请求异常, url: {}", url, e);
        }

        return null;
    }

    /**
     * 发送异步POST请求
     */
    public void doPostAsync(String url, String jsonBody, Callback callback) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 发送PUT请求
     */
    public String doPut(String url, String jsonBody) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .put(body)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
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
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error("DELETE请求异常, url: {}", url, e);
        }

        return null;
    }

}
