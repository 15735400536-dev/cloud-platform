package com.maxinhai.platform.feign;

import com.maxinhai.platform.fallback.ProduceFeignFallbackFactory;
import com.maxinhai.platform.utils.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cloud-platform-produce",
        //configuration = FeignConfig.class,
        fallbackFactory = ProduceFeignFallbackFactory.class)
public interface ProduceFeignClient {

    @GetMapping("/nacos/getConfig")
    AjaxResult<String> getConfig();

}
