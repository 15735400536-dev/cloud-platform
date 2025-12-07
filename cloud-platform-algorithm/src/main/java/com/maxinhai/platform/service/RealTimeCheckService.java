package com.maxinhai.platform.service;

import com.maxinhai.platform.bo.CheckResult;
import com.maxinhai.platform.dto.RealTimeCheckDTO;

public interface RealTimeCheckService {

    /**
     * 实时检测
     * @param param 参数
     * @return 检测结果
     */
    CheckResult realTimeCheck(RealTimeCheckDTO param);

    /**
     * 调用实时检测接口
     */
    void callRealTimeCheck();

    /**
     * 重试调用API
     */
    void retryCallApi(String apiUrl, RealTimeCheckDTO param);

}
