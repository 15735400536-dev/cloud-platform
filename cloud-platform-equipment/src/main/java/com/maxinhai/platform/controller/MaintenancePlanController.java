package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.MaintenancePlanAddDTO;
import com.maxinhai.platform.dto.MaintenancePlanEditDTO;
import com.maxinhai.platform.dto.MaintenancePlanQueryDTO;
import com.maxinhai.platform.service.MaintenancePlanService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.MaintenancePlanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：MaintenancePlanController
 * @Author: XinHai.Ma
 * @Date: 2025/10/10 15:14
 * @Description: 保养计划管理接口
 */
@RestController
@RequestMapping("/maintenancePlan")
@Api(tags = "保养计划管理接口")
public class MaintenancePlanController {

    @Resource
    private MaintenancePlanService maintenancePlanService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询保养计划信息", notes = "根据查询条件分页查询保养计划信息")
    public AjaxResult<PageResult<MaintenancePlanVO>> searchByPage(@RequestBody MaintenancePlanQueryDTO page) {
        return AjaxResult.success(PageResult.convert(maintenancePlanService.searchByPage(page)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取保养计划信息", notes = "根据保养计划ID获取详细信息")
    public AjaxResult<MaintenancePlanVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(maintenancePlanService.getInfo(id));
    }

    @PostMapping("/addPlan")
    @ApiOperation(value = "添加保养计划信息", notes = "添加保养计划信息")
    public AjaxResult<Void> addMaintenancePlan(@RequestBody MaintenancePlanAddDTO param) {
        maintenancePlanService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editPlan")
    @ApiOperation(value = "编辑保养计划信息", notes = "根据保养计划ID编辑保养计划信息")
    public AjaxResult<Void> editMaintenancePlan(@RequestBody MaintenancePlanEditDTO param) {
        maintenancePlanService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removePlan")
    @ApiOperation(value = "删除保养计划信息", notes = "根据保养计划ID数组删除保养计划信息")
    public AjaxResult<Void> removeMaintenancePlan(@RequestBody String[] ids) {
        maintenancePlanService.remove(ids);
        return AjaxResult.success();
    }

}
