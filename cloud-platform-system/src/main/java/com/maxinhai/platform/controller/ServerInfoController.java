package com.maxinhai.platform.controller;

import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ServerInfoUtils;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/server")
@Api(tags = "服务信息接口")
public class ServerInfoController {

    @GetMapping("/info")
    public AjaxResult<Map<String, Object>> getServerInfo() {
        return AjaxResult.success(ServerInfoUtils.getServerInfo());
    }

}
