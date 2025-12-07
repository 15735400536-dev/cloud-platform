package com.maxinhai.platform.service;

import com.maxinhai.platform.dto.RealTimeCheckDTO;

public interface RetryCallApiService {

    /**
     * 重试调用API
     * @param apiUrl API地址
     * @param param API参数
     */
    void retryCallApi(String apiUrl, RealTimeCheckDTO param);

}
