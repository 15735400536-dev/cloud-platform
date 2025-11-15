package com.maxinhai.platform.bo;

import com.maxinhai.platform.enums.ConnectType;
import com.maxinhai.platform.enums.ContentType;
import com.maxinhai.platform.enums.Method;
import com.maxinhai.platform.po.ApiConfig;
import com.maxinhai.platform.po.ConnectConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiClientBO {

    private String key;
    private ConnectType type;
    private String ip;
    private Integer port;
    private String username;
    private String password;
    private String apiKey;
    private Method method;
    private ContentType contentType;
    private String url;

    public static ApiClientBO build(ConnectConfig connectConfig, ApiConfig apiConfig) {
        ApiClientBO apiClient = new ApiClientBO();
        apiClient.setKey(connectConfig.getKey());
        apiClient.setType(connectConfig.getType());
        apiClient.setIp(connectConfig.getIp());
        apiClient.setPort(connectConfig.getPort());
        apiClient.setUsername(connectConfig.getUsername());
        apiClient.setPassword(connectConfig.getPassword());
        apiClient.setApiKey(apiConfig.getApiKey());
        apiClient.setMethod(apiConfig.getMethod());
        apiClient.setContentType(apiConfig.getContentType());
        apiClient.setUrl(apiConfig.getUrl());
        return apiClient;
    }

    public String getUrl() {
        return "http://" + this.ip + ":" + this.port + "/" + this.url;
    }

}
