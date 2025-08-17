package com.maxinhai.platform.controller;

import com.maxinhai.platform.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/nacos")
@Api(tags = "Nacos配置管理接口")
public class NacosConfigController {

    @Value("${service.name:unknown}")
    private String nacosConfig;

    @GetMapping("/getConfig")
    @ApiOperation(value = "获取nacos配置文件配置", notes = "获取nacos配置文件配置")
    public AjaxResult<String> getConfig() {
        return AjaxResult.success(nacosConfig);
    }

}
