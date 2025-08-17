package com.maxinhai.platform.controller;

import com.maxinhai.platform.service.ZlmClient;
import com.maxinhai.platform.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/zlm")
public class ZlmController {

    @Resource
    private ZlmClient zlmClient;
    @Value("${secret:re9VTQe70OiWZ3NrY4L6Eolpy3hlGOoh}")
    private String secret;

    @GetMapping("/getApiList")
    public AjaxResult<Map<String, Object>> getApiList() {
        return AjaxResult.success(zlmClient.getApiList(secret));
    }

    @GetMapping("/getVersion")
    public AjaxResult<Map<String, Object>> getVersion() {
        return AjaxResult.success(zlmClient.getVersion(secret));
    }

}
