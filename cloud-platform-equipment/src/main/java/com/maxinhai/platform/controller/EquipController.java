package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.EquipmentAddDTO;
import com.maxinhai.platform.dto.EquipmentEditDTO;
import com.maxinhai.platform.dto.EquipmentQueryDTO;
import com.maxinhai.platform.service.EquipmentService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.EquipmentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/equip")
@Api(tags = "设备管理接口")
public class EquipController {

    @Resource
    private EquipmentService equipService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询设备信息", notes = "根据查询条件分页查询设备信息")
    public AjaxResult<PageResult<EquipmentVO>> searchByPage(@RequestBody EquipmentQueryDTO page) {
        return AjaxResult.success(PageResult.convert(equipService.searchByPage(page)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取设备信息", notes = "根据设备ID获取详细信息")
    public AjaxResult<EquipmentVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(equipService.getInfo(id));
    }

    @PostMapping("/addEquip")
    @ApiOperation(value = "添加设备信息", notes = "添加设备信息")
    public AjaxResult<Void> addEquipment(@RequestBody EquipmentAddDTO param) {
        equipService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editEquip")
    @ApiOperation(value = "编辑设备信息", notes = "根据设备ID编辑设备信息")
    public AjaxResult<Void> editEquipment(@RequestBody EquipmentEditDTO param) {
        equipService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeEquip")
    @ApiOperation(value = "删除设备信息", notes = "根据设备ID数组删除设备信息")
    public AjaxResult<Void> removeEquipment(@RequestBody String[] ids) {
        equipService.remove(ids);
        return AjaxResult.success();
    }
}
