package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.MaintenanceItemAddDTO;
import com.maxinhai.platform.dto.MaintenanceItemEditDTO;
import com.maxinhai.platform.dto.MaintenanceItemQueryDTO;
import com.maxinhai.platform.service.MaintenanceItemService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.MaintenanceItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：MaintenanceItemController
 * @Author: XinHai.Ma
 * @Date: 2025/10/10 15:14
 * @Description: 保养项目管理接口
 */
@RestController
@RequestMapping("/maintenanceItem")
@Api(tags = "保养项目管理接口")
public class MaintenanceItemController {

    @Resource
    private MaintenanceItemService maintenanceItemService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询保养项目信息", notes = "根据查询条件分页查询保养项目信息")
    public AjaxResult<PageResult<MaintenanceItemVO>> searchByPage(@RequestBody MaintenanceItemQueryDTO page) {
        return AjaxResult.success(PageResult.convert(maintenanceItemService.searchByPage(page)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取保养项目信息", notes = "根据保养项目ID获取详细信息")
    public AjaxResult<MaintenanceItemVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(maintenanceItemService.getInfo(id));
    }

    @PostMapping("/addItem")
    @ApiOperation(value = "添加保养项目信息", notes = "添加保养项目信息")
    public AjaxResult<Void> addMaintenanceItem(@RequestBody MaintenanceItemAddDTO param) {
        maintenanceItemService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editItem")
    @ApiOperation(value = "编辑保养项目信息", notes = "根据保养项目ID编辑保养项目信息")
    public AjaxResult<Void> editMaintenanceItem(@RequestBody MaintenanceItemEditDTO param) {
        maintenanceItemService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeItem")
    @ApiOperation(value = "删除保养项目信息", notes = "根据保养项目ID数组删除保养项目信息")
    public AjaxResult<Void> removeMaintenanceItem(@RequestBody String[] ids) {
        maintenanceItemService.remove(ids);
        return AjaxResult.success();
    }

}
