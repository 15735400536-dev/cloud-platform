package com.maxinhai.platform.handler;

import com.maxinhai.platform.mapper.ApiConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName：ApiHandler
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 11:39
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Component
public class ApiHandler {

    @Resource
    private ApiConfigMapper apiConfigMapper;

    public void execute(String connectKey, String configKey, Object request, Object response) {

    }

}
