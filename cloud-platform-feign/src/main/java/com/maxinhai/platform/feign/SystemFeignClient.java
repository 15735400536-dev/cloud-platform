package com.maxinhai.platform.feign;

import com.maxinhai.platform.bo.UserBO;
import com.maxinhai.platform.dto.UserAddDTO;
import com.maxinhai.platform.fallback.SystemFeignFallbackFactory;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.DataDictVO;
import com.maxinhai.platform.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "cloud-platform-system",
        //configuration = FeignConfig.class,
        fallbackFactory = SystemFeignFallbackFactory.class)
public interface SystemFeignClient {

    @GetMapping("/nacos/getConfig")
    AjaxResult<String> getConfig();

    @GetMapping("/dataDict/getDataDict/{dictType}")
    AjaxResult<List<DataDictVO>> getDataDict(@PathVariable("dictType") String dictType);

    @GetMapping("/user/findByAccount/{account}")
    AjaxResult<UserVO> findByAccount(@PathVariable("account") String account);

    @GetMapping("/user/existByUsername/{username}")
    AjaxResult<Boolean> existByUsername(@PathVariable("username") String username);

    @GetMapping("/user/existByAccount/{account}")
    AjaxResult<Boolean> existByAccount(@PathVariable("account") String account);

    @PostMapping("/user/addUser")
    AjaxResult<Void> addUser(@RequestBody UserAddDTO param);

    @GetMapping("/user/getUserList")
    AjaxResult<List<UserBO>> getUserList();

    @GetMapping("/user/getUserMap}")
    AjaxResult<Map<String, String>> getUserMap();

    @GetMapping("/codeRule/generateCode/{codeRule}/{batchSize}")
    AjaxResult<List<String>> generateCode(@PathVariable("codeRule") String codeRule,
                                          @PathVariable("batchSize") Integer batchSize);

}
