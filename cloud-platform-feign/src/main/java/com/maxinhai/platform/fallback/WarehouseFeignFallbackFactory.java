package com.maxinhai.platform.fallback;

import com.maxinhai.platform.feign.WarehouseFeignClient;
import com.maxinhai.platform.utils.AjaxResult;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class WarehouseFeignFallbackFactory implements FallbackFactory<WarehouseFeignClient> {

    @Override
    public WarehouseFeignClient create(Throwable cause) {
        return new WarehouseFeignClient() {

            @Override
            public AjaxResult<String> getConfig() {
                return AjaxResult.fail("服务调用失败!", null);
            }
        };
    }
}
