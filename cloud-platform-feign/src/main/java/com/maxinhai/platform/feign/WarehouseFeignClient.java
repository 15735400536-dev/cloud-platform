package com.maxinhai.platform.feign;

import com.maxinhai.platform.fallback.WarehouseFeignFallbackFactory;
import com.maxinhai.platform.utils.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cloud-platform-warehouse",
        //configuration = FeignConfig.class,
        fallbackFactory = WarehouseFeignFallbackFactory.class)
public interface WarehouseFeignClient {

    @GetMapping("/nacos/getConfig")
    AjaxResult<String> getConfig();

}
