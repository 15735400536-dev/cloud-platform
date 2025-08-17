package com.maxinhai.platform.feign;

import com.maxinhai.platform.fallback.EquipmentFeignFallbackFactory;
import com.maxinhai.platform.utils.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cloud-platform-equipment",
        //configuration = FeignConfig.class,
        fallbackFactory = EquipmentFeignFallbackFactory.class)
public interface EquipmentFeignClient {

    @GetMapping("/nacos/getConfig")
    AjaxResult<String> getConfig();

}
