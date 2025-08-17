package com.maxinhai.platform.feign;

import com.maxinhai.platform.fallback.MdmFeignFallbackFactory;
import com.maxinhai.platform.utils.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cloud-platform-mdm",
        //configuration = FeignConfig.class,
        fallbackFactory = MdmFeignFallbackFactory.class)
public interface MdmFeignClient {

    @GetMapping("/nacos/getConfig")
    AjaxResult<String> getConfig();

}
