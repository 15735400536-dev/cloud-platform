package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.InspectionPlanAddDTO;
import com.maxinhai.platform.dto.InspectionPlanEditDTO;
import com.maxinhai.platform.dto.InspectionPlanQueryDTO;
import com.maxinhai.platform.service.InspectionPlanService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.InspectionPlanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：InspectionPlanController
 * @Author: XinHai.Ma
 * @Date: 2025/10/10 15:13
 * @Description: 巡检计划管理接口
 */
@RestController
@RequestMapping("/equip")
@Api(tags = "巡检计划管理接口")
public class InspectionPlanController {

    @Resource
    private InspectionPlanService inspectionPlanService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询巡检计划信息", notes = "根据查询条件分页查询巡检计划信息")
    public AjaxResult<PageResult<InspectionPlanVO>> searchByPage(@RequestBody InspectionPlanQueryDTO page) {
        return AjaxResult.success(PageResult.convert(inspectionPlanService.searchByPage(page)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取巡检计划信息", notes = "根据巡检计划ID获取详细信息")
    public AjaxResult<InspectionPlanVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(inspectionPlanService.getInfo(id));
    }

    @PostMapping("/addPlan")
    @ApiOperation(value = "添加巡检计划信息", notes = "添加巡检计划信息")
    public AjaxResult<Void> addInspectionPlan(@RequestBody InspectionPlanAddDTO param) {
        inspectionPlanService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editPlan")
    @ApiOperation(value = "编辑巡检计划信息", notes = "根据巡检计划ID编辑巡检计划信息")
    public AjaxResult<Void> editInspectionPlan(@RequestBody InspectionPlanEditDTO param) {
        inspectionPlanService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removePlan")
    @ApiOperation(value = "删除巡检计划信息", notes = "根据巡检计划ID数组删除巡检计划信息")
    public AjaxResult<Void> removeInspectionPlan(@RequestBody String[] ids) {
        inspectionPlanService.remove(ids);
        return AjaxResult.success();
    }
    
}
