package com.maxinhai.platform.feign;

import com.maxinhai.platform.fallback.SystemFeignFallbackFactory;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.DataDictVO;
import com.maxinhai.platform.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "cloud-platform-system",
        //configuration = FeignConfig.class,
        fallbackFactory = SystemFeignFallbackFactory.class)
public interface SystemFeignClient {

    @GetMapping("/nacos/getConfig")
    AjaxResult<String> getConfig();

    @GetMapping("/dataDict/getDataDict/{dictType}")
    AjaxResult<List<DataDictVO>> getDataDict(@PathVariable("dictType") String dictType);

    @GetMapping("/user/findByAccount/{account}")
    AjaxResult<UserVO> findByAccount(String account);

    @GetMapping("/codeRule/generateCode/{codeRule}/{batchSize}")
    AjaxResult<List<String>> generateCode(@PathVariable("codeRule") String codeRule,
                                          @PathVariable("batchSize") Integer batchSize);

}
