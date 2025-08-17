package com.maxinhai.platform.fallback;

import com.maxinhai.platform.feign.EquipmentFeignClient;
import com.maxinhai.platform.utils.AjaxResult;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class EquipmentFeignFallbackFactory implements FallbackFactory<EquipmentFeignClient> {

    @Override
    public EquipmentFeignClient create(Throwable cause) {
        return new EquipmentFeignClient() {

            @Override
            public AjaxResult<String> getConfig() {
                return AjaxResult.fail("服务调用失败!", null);
            }
        };
    }
}
