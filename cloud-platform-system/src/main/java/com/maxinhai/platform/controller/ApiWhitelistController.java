package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.ApiWhitelistAddDTO;
import com.maxinhai.platform.dto.ApiWhitelistEditDTO;
import com.maxinhai.platform.dto.ApiWhitelistQueryDTO;
import com.maxinhai.platform.service.ApiWhitelistService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.ApiWhitelistVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/whitelist")
@Api(tags = "接口白名单管理接口")
public class ApiWhitelistController {

    @Resource
    private ApiWhitelistService apiWhitelistService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询接口白名单信息", notes = "根据查询条件分页查询接口白名单信息")
    public AjaxResult<PageResult<ApiWhitelistVO>> searchByPage(@RequestBody ApiWhitelistQueryDTO param) {
        return AjaxResult.success(PageResult.convert(apiWhitelistService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取接口白名单信息", notes = "根据接口白名单ID获取详细信息")
    public AjaxResult<ApiWhitelistVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(apiWhitelistService.getInfo(id));
    }

    @PostMapping("/addApiWhitelist")
    @ApiOperation(value = "添加接口白名单信息", notes = "添加接口白名单信息")
    public AjaxResult<Void> addApiWhitelist(@RequestBody ApiWhitelistAddDTO param) {
        apiWhitelistService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editApiWhitelist")
    @ApiOperation(value = "编辑接口白名单信息", notes = "根据接口白名单ID编辑接口白名单信息")
    public AjaxResult<Void> editApiWhitelist(@RequestBody ApiWhitelistEditDTO param) {
        apiWhitelistService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeApiWhitelist")
    @ApiOperation(value = "删除接口白名单信息", notes = "根据接口白名单ID数组删除接口白名单信息")
    public AjaxResult<Void> removeApiWhitelist(@RequestBody String[] ids) {
        apiWhitelistService.remove(ids);
        return AjaxResult.success();
    }
}
