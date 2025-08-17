package com.maxinhai.platform.controller;

import com.maxinhai.platform.feign.EquipmentFeignClient;
import com.maxinhai.platform.feign.MdmFeignClient;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.feign.WarehouseFeignClient;
import com.maxinhai.platform.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/nacos")
@Api(tags = "Nacos配置管理接口")
public class NacosConfigController {

    @Value("${service.name:unknown}")
    private String nacosConfig;
    @Resource
    private SystemFeignClient systemFeignClient;
    @Resource
    private MdmFeignClient mdmFeignClient;
    @Resource
    private WarehouseFeignClient warehouseFeignClient;
    @Resource
    private EquipmentFeignClient equipmentFeignClient;

    @GetMapping("/getConfig")
    @ApiOperation(value = "获取nacos配置文件配置", notes = "获取nacos配置文件配置")
    public AjaxResult<String> getConfig() {
        return AjaxResult.success(nacosConfig);
    }

    //@Scheduled(initialDelay = 5000, fixedDelay = 60000)
    public void callback() {
        AjaxResult<String> result = systemFeignClient.getConfig();
        log.info("systemFeignClient:{}", result.getData());

        result = mdmFeignClient.getConfig();
        log.info("mdmFeignClient:{}", result.getData());

        result = warehouseFeignClient.getConfig();
        log.info("warehouseFeignClient:{}", result.getData());

        result = equipmentFeignClient.getConfig();
        log.info("equipmentFeignClient:{}", result.getData());

//        AjaxResult<List<DataDictVO>> feignResult = systemFeignClient.getDataDict("sex");
//        log.info("systemFeignClient:{}", feignResult.getData());
    }

}
