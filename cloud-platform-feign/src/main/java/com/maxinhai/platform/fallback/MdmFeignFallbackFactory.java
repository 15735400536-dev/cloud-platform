package com.maxinhai.platform.fallback;

import com.maxinhai.platform.feign.MdmFeignClient;
import com.maxinhai.platform.utils.AjaxResult;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MdmFeignFallbackFactory implements FallbackFactory<MdmFeignClient> {

    @Override
    public MdmFeignClient create(Throwable cause) {
        return new MdmFeignClient() {

            @Override
            public AjaxResult<String> getConfig() {
                return AjaxResult.fail("服务调用失败!", null);
            }
        };
    }
}
