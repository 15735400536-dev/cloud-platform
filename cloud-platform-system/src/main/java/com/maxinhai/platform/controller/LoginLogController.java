package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.LoginLogQueryDTO;
import com.maxinhai.platform.service.LoginLogService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.LoginLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loginLog")
@Api(tags = "登录日志管理接口")
@RequiredArgsConstructor
public class LoginLogController {

    private final LoginLogService loginLogService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询登录日志", notes = "根据查询条件分页查询登录日志")
    public AjaxResult<PageResult<LoginLogVO>> searchByPage(@RequestBody LoginLogQueryDTO param) {
        return AjaxResult.success(PageResult.convert(loginLogService.searchByPage(param)));
    }

}
