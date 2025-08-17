package com.maxinhai.platform.fallback;

import com.maxinhai.platform.feign.ProduceFeignClient;
import com.maxinhai.platform.utils.AjaxResult;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ProduceFeignFallbackFactory implements FallbackFactory<ProduceFeignClient> {

    @Override
    public ProduceFeignClient create(Throwable cause) {
        return new ProduceFeignClient() {

            @Override
            public AjaxResult<String> getConfig() {
                return AjaxResult.fail("服务调用失败!", null);
            }
        };
    }
}
