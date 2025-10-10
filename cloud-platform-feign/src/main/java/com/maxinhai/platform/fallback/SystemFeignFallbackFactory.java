package com.maxinhai.platform.fallback;

import com.maxinhai.platform.dto.UserAddDTO;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.vo.DataDictVO;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.UserVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SystemFeignFallbackFactory implements FallbackFactory<SystemFeignClient> {

    @Override
    public SystemFeignClient create(Throwable cause) {
        return new SystemFeignClient() {

            @Override
            public AjaxResult<String> getConfig() {
                return AjaxResult.fail("服务调用失败!");
            }

            @Override
            public AjaxResult<List<DataDictVO>> getDataDict(String dictType) {
                return AjaxResult.fail("服务调用失败!");
            }

            @Override
            public AjaxResult<UserVO> findByAccount(String account) {
                return AjaxResult.fail("服务调用失败!");
            }

            @Override
            public AjaxResult<List<String>> generateCode(String codeRule, Integer batchSize) {
                return AjaxResult.fail("服务调用失败!");
            }

            @Override
            public AjaxResult<Boolean> existByUsername(String username) {
                return AjaxResult.fail("服务调用失败!");
            }

            @Override
            public AjaxResult<Boolean> existByAccount(String account) {
                return AjaxResult.fail("服务调用失败!");
            }

            @Override
            public AjaxResult<Void> addUser(UserAddDTO param) {
                return AjaxResult.fail("服务调用失败!");
            }
        };
    }
}
