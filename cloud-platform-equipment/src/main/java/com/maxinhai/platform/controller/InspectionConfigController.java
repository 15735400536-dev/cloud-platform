package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.InspectionConfigAddDTO;
import com.maxinhai.platform.dto.InspectionConfigEditDTO;
import com.maxinhai.platform.dto.InspectionConfigQueryDTO;
import com.maxinhai.platform.service.InspectionConfigService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.InspectionConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：InspectionConfigController
 * @Author: XinHai.Ma
 * @Date: 2025/10/10 15:12
 * @Description: 巡检配置管理接口
 */
@RestController
@RequestMapping("/inspectionConfig")
@Api(tags = "巡检配置管理接口")
public class InspectionConfigController {

    @Resource
    private InspectionConfigService inspectionConfigService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询巡检配置信息", notes = "根据查询条件分页查询巡检配置信息")
    public AjaxResult<PageResult<InspectionConfigVO>> searchByPage(@RequestBody InspectionConfigQueryDTO page) {
        return AjaxResult.success(PageResult.convert(inspectionConfigService.searchByPage(page)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取巡检配置信息", notes = "根据巡检配置ID获取详细信息")
    public AjaxResult<InspectionConfigVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(inspectionConfigService.getInfo(id));
    }

    @PostMapping("/addConfig")
    @ApiOperation(value = "添加巡检配置信息", notes = "添加巡检配置信息")
    public AjaxResult<Void> addInspectionConfig(@RequestBody InspectionConfigAddDTO param) {
        inspectionConfigService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editConfig")
    @ApiOperation(value = "编辑巡检配置信息", notes = "根据巡检配置ID编辑巡检配置信息")
    public AjaxResult<Void> editInspectionConfig(@RequestBody InspectionConfigEditDTO param) {
        inspectionConfigService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeConfig")
    @ApiOperation(value = "删除巡检配置信息", notes = "根据巡检配置ID数组删除巡检配置信息")
    public AjaxResult<Void> removeInspectionConfig(@RequestBody String[] ids) {
        inspectionConfigService.remove(ids);
        return AjaxResult.success();
    }
    
}
