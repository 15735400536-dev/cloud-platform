package com.maxinhai.platform.handler;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.bo.ApiClientBO;
import com.maxinhai.platform.enums.ConnectType;
import com.maxinhai.platform.enums.ContentType;
import com.maxinhai.platform.enums.Method;
import com.maxinhai.platform.mapper.ApiConfigMapper;
import com.maxinhai.platform.mapper.ConnectConfigMapper;
import com.maxinhai.platform.po.ApiConfig;
import com.maxinhai.platform.po.ConnectConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName：ApiHandler
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 11:39
 * @Description: API接口处理器
 */
@Slf4j
@Component
public class ApiHandler implements CommandLineRunner {

    @Resource
    private ConnectConfigMapper connectMapper;
    @Resource
    private ApiConfigMapper apiMapper;
    @Resource
    private OkHttpClient okHttpClient;
    // 客户端配置
    private final Map<String, ApiClientBO> apiClientMap = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化API客户端");
        initClients();
        log.info("API客户端初始化完成");
    }

    /**
     * 初始化全部客户端
     */
    public void initClients() {
        List<ConnectConfig> connectList = connectMapper.selectList(new LambdaQueryWrapper<ConnectConfig>()
                .select(ConnectConfig::getId, ConnectConfig::getType, ConnectConfig::getKey,
                        ConnectConfig::getIp, ConnectConfig::getPort, ConnectConfig::getUsername, ConnectConfig::getPassword)
                .eq(ConnectConfig::getType, ConnectType.API));
        if (connectList.isEmpty()) {
            return;
        }
        Map<String, ConnectConfig> connectMap = connectList.stream().collect(Collectors.toMap(ConnectConfig::getId, ConnectConfig -> ConnectConfig));
        List<String> connectIds = connectList.stream().map(ConnectConfig::getId).collect(Collectors.toList());
        List<ApiConfig> apiConfigList = apiMapper.selectList(new LambdaQueryWrapper<ApiConfig>()
                .select(ApiConfig::getConnectId, ApiConfig::getApiKey, ApiConfig::getUrl, ApiConfig::getMethod, ApiConfig::getContentType)
                .in(ApiConfig::getConnectId, connectIds));

        for (ApiConfig apiConfig : apiConfigList) {
            ApiClientBO build = ApiClientBO.build(connectMap.get(apiConfig.getConnectId()), apiConfig);
            apiClientMap.putIfAbsent(build.getApiKey(), build);
        }
    }

    public String execute(String apiKey, Map<String, Object> request, Class<T> response) throws IOException {
        if (apiClientMap.isEmpty()) {
            throw new IllegalStateException("没有可用的API客户端");
        }
        ApiClientBO client = apiClientMap.get(apiKey);
        if (client == null) {
            throw new IllegalArgumentException("未找到客户端（apiKey: " + apiKey + "）");
        }
        return request(client.getUrl(), client.getMethod(), client.getContentType(), request, response);
    }

    /**
     * 通用HTTP请求方法（支持GET/POST/PUT/DELETE及所有媒体类型）
     *
     * @param url         请求地址
     * @param method      HTTP方法（GET/POST/PUT/DELETE）
     * @param params      参数（GET/DELETE为查询参数；POST/PUT为请求体数据）
     * @param contentType 媒体类型（仅POST/PUT有效）
     * @return 响应结果字符串
     */
    public String request(String url, Method method, ContentType contentType, Map<String, Object> params, Class<T> targetClass) throws IOException {
        // 1. 构建基础请求Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        // 2. 处理查询参数（GET/DELETE适用）
        if (method == Method.GET || method == Method.DELETE) {
            url = buildUrlWithQueryParams(url, params);
            requestBuilder.url(url); // 更新带查询参数的URL
        }

        // 3. 处理请求体（POST/PUT适用）
        RequestBody requestBody = null;
        if (method == Method.POST || method == Method.PUT) {
            requestBody = buildRequestBody(params, contentType);
        }

        // 4. 设置HTTP方法（绑定请求体）
        switch (method) {
            case GET:
                requestBuilder.get();
                break;
            case POST:
                requestBuilder.post(requestBody);
                break;
            case PUT:
                requestBuilder.put(requestBody);
                break;
            case DELETE:
                // DELETE可以带请求体（可选，此处支持无请求体）
                requestBuilder.delete(requestBody);
                break;
        }

        // 5. 执行请求并处理响应
        try (Response response = okHttpClient.newCall(requestBuilder.build()).execute()) {
            return handleResponse(response);
        }
    }

    // ------------------------------ 私有工具方法 ------------------------------

    /**
     * 为URL添加查询参数（GET/DELETE）
     *
     * @param url         原始URL
     * @param queryParams 查询参数（需为Map<String, String>，null则不添加）
     * @return 带查询参数的URL
     */
    private String buildUrlWithQueryParams(String url, Map<String, Object> queryParams) {
        if (queryParams == null) {
            return url;
        }
        // 校验参数类型（GET/DELETE查询参数必须是Map<String, String>）
        if (!(queryParams instanceof Map)) {
            throw new IllegalArgumentException("GET/DELETE请求的参数必须是Map<String, String>");
        }
        if (queryParams.isEmpty()) {
            return url;
        }

        // 构建带查询参数的URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        queryParams.forEach((key, value) -> urlBuilder.addQueryParameter(key, String.valueOf(value)));
        return urlBuilder.build().toString();
    }

    /**
     * 构建请求体（POST/PUT）
     *
     * @param data        请求体数据（根据媒体类型传对应格式）
     * @param connectType 媒体类型
     */
    private RequestBody buildRequestBody(Object data, ContentType connectType) {
        if (data == null) {
            return RequestBody.create("", MediaType.parse(connectType.getMediaType()));
        }

        switch (connectType) {
            case APPLICATION_FORM_URLENCODED:
                // 表单数据（键值对）：data需为Map<String, String>
                FormBody.Builder formBuilder = new FormBody.Builder();
                ((Map<String, String>) data).forEach(formBuilder::add);
                return formBuilder.build();

            case MULTIPART_FORM_DATA:
                // 多部分表单：data需为MultipartBody.Builder
                if (data instanceof MultipartBody.Builder) {
                    return ((MultipartBody.Builder) data).build();
                } else {
                    throw new IllegalArgumentException("MULTIPART_FORM_DATA类型的数据必须是MultipartBody.Builder");
                }

            case APPLICATION_JSON:
                // JSON数据：data可为对象（自动转JSON）或JSON字符串
                String json = data instanceof String ? (String) data : JSON.toJSONString(data);
                return RequestBody.create(json, MediaType.parse(connectType.getMediaType()));

            case TEXT_PLAIN:
            case TEXT_HTML:
            case APPLICATION_XML:
                // 文本类型：data需为字符串
                return RequestBody.create((String) data, MediaType.parse(connectType.getMediaType()));

            case IMAGE_JPEG:
            case IMAGE_PNG:
                // 图片文件：data需为File或MultipartFile
                if (data instanceof File) {
                    return RequestBody.create((File) data, MediaType.parse(connectType.getMediaType()));
                } else if (data instanceof MultipartFile) {
                    try {
                        return RequestBody.create(((MultipartFile) data).getBytes(), MediaType.parse("image/jpeg"));
                    } catch (IOException e) {
                        throw new RuntimeException("文件转换失败", e);
                    }
                } else {
                    throw new IllegalArgumentException("图片类型的数据必须是File或MultipartFile");
                }

            case APPLICATION_OCTET_STREAM:
                // 二进制流：data需为byte[]或File
                if (data instanceof byte[]) {
                    return RequestBody.create((byte[]) data, MediaType.parse(connectType.getMediaType()));
                } else if (data instanceof File) {
                    return RequestBody.create((File) data, MediaType.parse(connectType.getMediaType()));
                } else {
                    throw new IllegalArgumentException("二进制流类型的数据必须是byte[]或File");
                }

            default:
                throw new IllegalArgumentException("不支持的媒体类型：" + connectType);
        }
    }

    /**
     * 处理响应结果
     */
    private String handleResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("请求失败，状态码：" + response.code() + "，响应：" + response.message());
        }
        ResponseBody body = response.body();
        return body != null ? body.string() : "";
    }

    // ------------------------------ 辅助方法：构建多部分表单 ------------------------------

    /**
     * 构建多部分表单（用于MULTIPART_FORM_DATA类型）
     */
    public MultipartBody.Builder buildMultipartBody(
            Map<String, String> params,
            Map<String, File> fileParams) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data"));

        // 添加普通字段
        if (params != null) {
            params.forEach((key, value) -> builder.addFormDataPart(key, value));
        }

        // 添加文件字段
        if (fileParams != null) {
            fileParams.forEach((key, file) -> {
                builder.addFormDataPart(
                        key,
                        file.getName(),
                        RequestBody.create(file, MediaType.parse(guessMediaType(file.getName())))
                );
            });
        }
        return builder;
    }

    /**
     * 猜测文件媒体类型（根据后缀）
     */
    private String guessMediaType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return ContentType.IMAGE_JPEG.toString();
        } else if (fileName.endsWith(".png")) {
            return ContentType.IMAGE_PNG.toString();
        } else {
            return ContentType.APPLICATION_OCTET_STREAM.toString();
        }
    }

}
