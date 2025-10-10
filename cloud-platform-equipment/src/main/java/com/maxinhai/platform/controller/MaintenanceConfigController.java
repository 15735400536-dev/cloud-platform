package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.MaintenanceConfigAddDTO;
import com.maxinhai.platform.dto.MaintenanceConfigEditDTO;
import com.maxinhai.platform.dto.MaintenanceConfigQueryDTO;
import com.maxinhai.platform.service.MaintenanceConfigService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.MaintenanceConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：MaintenanceConfigController
 * @Author: XinHai.Ma
 * @Date: 2025/10/10 15:14
 * @Description: 保养配置管理接口
 */
@RestController
@RequestMapping("/maintenanceConfig")
@Api(tags = "保养配置管理接口")
public class MaintenanceConfigController {

    @Resource
    private MaintenanceConfigService maintenanceConfigService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询保养配置信息", notes = "根据查询条件分页查询保养配置信息")
    public AjaxResult<PageResult<MaintenanceConfigVO>> searchByPage(@RequestBody MaintenanceConfigQueryDTO page) {
        return AjaxResult.success(PageResult.convert(maintenanceConfigService.searchByPage(page)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取保养配置信息", notes = "根据保养配置ID获取详细信息")
    public AjaxResult<MaintenanceConfigVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(maintenanceConfigService.getInfo(id));
    }

    @PostMapping("/addConfig")
    @ApiOperation(value = "添加保养配置信息", notes = "添加保养配置信息")
    public AjaxResult<Void> addMaintenanceConfig(@RequestBody MaintenanceConfigAddDTO param) {
        maintenanceConfigService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editConfig")
    @ApiOperation(value = "编辑保养配置信息", notes = "根据保养配置ID编辑保养配置信息")
    public AjaxResult<Void> editMaintenanceConfig(@RequestBody MaintenanceConfigEditDTO param) {
        maintenanceConfigService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeConfig")
    @ApiOperation(value = "删除保养配置信息", notes = "根据保养配置ID数组删除保养配置信息")
    public AjaxResult<Void> removeMaintenanceConfig(@RequestBody String[] ids) {
        maintenanceConfigService.remove(ids);
        return AjaxResult.success();
    }

}
