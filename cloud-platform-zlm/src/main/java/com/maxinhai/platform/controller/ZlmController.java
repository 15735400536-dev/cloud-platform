package com.maxinhai.platform.controller;

import com.alibaba.fastjson2.JSONObject;
import com.maxinhai.platform.dto.AddStreamProxyDTO;
import com.maxinhai.platform.dto.DelStreamProxyDTO;
import com.maxinhai.platform.dto.ServiceConfigDTO;
import com.maxinhai.platform.service.ZlmClient;
import com.maxinhai.platform.utils.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/zlm")
public class ZlmController {

    @Resource
    private ZlmClient zlmClient;

    private static ServiceConfigDTO serviceConfig = null;
    static {
        if(Objects.isNull(serviceConfig)){
            serviceConfig = new ServiceConfigDTO("127.0.0.1", 8080, "EQPSv4wFVeS7e7hJ9YyC3s1Zbs8Njn78");
        }
    }

    @GetMapping("/getApiList")
    public AjaxResult<JSONObject> getApiList() {
        return AjaxResult.success(zlmClient.getApiList(serviceConfig));
    }

    @GetMapping("/getVersion")
    public AjaxResult<JSONObject> getVersion() {
        return AjaxResult.success(zlmClient.version(serviceConfig));
    }

    @GetMapping("/getThreadsLoad")
    public AjaxResult<JSONObject> getThreadsLoad() {
        return AjaxResult.success(zlmClient.getThreadsLoad(serviceConfig));
    }

    @GetMapping("/getWorkThreadsLoad")
    public AjaxResult<JSONObject> getWorkThreadsLoad() {
        return AjaxResult.success(zlmClient.getWorkThreadsLoad(serviceConfig));
    }

    @GetMapping("/getStatistic")
    public AjaxResult<JSONObject> getStatistic() {
        return AjaxResult.success(zlmClient.getStatistic(serviceConfig));
    }

    @GetMapping("/getServerConfig")
    public AjaxResult<JSONObject> getServerConfig() {
        return AjaxResult.success(zlmClient.getServerConfig(serviceConfig));
    }

    @GetMapping("/getMediaList")
    public AjaxResult<JSONObject> getMediaList() {
        return AjaxResult.success(zlmClient.getMediaList(serviceConfig));
    }

    @GetMapping("/getAllSession")
    public AjaxResult<JSONObject> getAllSession() {
        return AjaxResult.success(zlmClient.getAllSession(serviceConfig));
    }

    @GetMapping("/restartServer")
    public AjaxResult<JSONObject> restartServer() {
        return AjaxResult.success(zlmClient.restartServer(serviceConfig));
    }

    @GetMapping("/addStreamProxy")
    public AjaxResult<JSONObject> addStreamProxy() {
        AddStreamProxyDTO param = new AddStreamProxyDTO("_defaultVhost_", "demo", "01", "rtsp://127.0.0.1:8554/rtsp");
        AddStreamProxyDTO.setConfig(param, serviceConfig);
        return AjaxResult.success(zlmClient.addStreamProxy(param));
    }

    @GetMapping("/delStreamProxy")
    public AjaxResult<JSONObject> delStreamProxy(@PathVariable("key") String key) {
        DelStreamProxyDTO param = new DelStreamProxyDTO(key);
        DelStreamProxyDTO.setConfig(param, serviceConfig);
        return AjaxResult.success(zlmClient.delStreamProxy(param));
    }

}
