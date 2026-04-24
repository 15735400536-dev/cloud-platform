package com.maxinhai.platform.service;

import java.util.Map;

public interface OperatorService {

    /**
     * 从Redis获取操作人集合
     * @return 操作人集合
     */
    Map<String, String> getOperator();

    /**
     * 根据操作人ID从Redis获取操作人
     * @param userId 用户ID
     * @return 操作人
     */
    String getOperator(String userId);

}
