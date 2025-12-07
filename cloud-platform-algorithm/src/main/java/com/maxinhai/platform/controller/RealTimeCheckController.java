package com.maxinhai.platform.controller;

import com.google.common.collect.Lists;
import com.maxinhai.platform.bo.CheckResult;
import com.maxinhai.platform.dto.RealTimeCheckDTO;
import com.maxinhai.platform.service.RealTimeCheckService;
import com.maxinhai.platform.service.RetryCallApiService;
import com.maxinhai.platform.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/algorithm")
@Api(tags = "设备管理接口")
@RequiredArgsConstructor
public class RealTimeCheckController {

    private final RealTimeCheckService realTimeCheckService;
    private final RetryCallApiService retryCallApi;

    @PostMapping(value = "realTimeCheck")
    @ApiOperation(value = "算法实时检测", notes = "根据图片算法实时检测")
    public AjaxResult<CheckResult> realTimeCheck(@RequestBody RealTimeCheckDTO param) {
        return AjaxResult.success(realTimeCheckService.realTimeCheck(param));
    }

    @PostMapping(value = "callRealTimeCheck")
    @ApiOperation(value = "调用实时检测接口", notes = "调用实时检测接口")
    public AjaxResult<Void> callRealTimeCheck() {
        realTimeCheckService.callRealTimeCheck();
        return AjaxResult.success();
    }

    @PostMapping(value = "retryCallApi")
    @ApiOperation(value = "重试调用API", notes = "重试调用API")
    public AjaxResult<Void> retryCallApi() {
        String apiUrl = "http://localhost:10090/algorithm/realTimeCheck";
        RealTimeCheckDTO param = new RealTimeCheckDTO();
        param.setImages(Lists.newArrayList("1.jpg", "2.jpg", "3.jpg"));
        retryCallApi.retryCallApi(apiUrl, param);
        return AjaxResult.success();
    }

}
