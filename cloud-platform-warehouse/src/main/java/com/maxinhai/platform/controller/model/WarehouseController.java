package com.maxinhai.platform.controller.model;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.model.WarehouseAddDTO;
import com.maxinhai.platform.dto.model.WarehouseEditDTO;
import com.maxinhai.platform.dto.model.WarehouseQueryDTO;
import com.maxinhai.platform.po.ComboBox;
import com.maxinhai.platform.enums.Status;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.service.model.WarehouseService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ComboBoxUtils;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.model.WarehouseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/warehouse")
@Api(tags = "仓库管理接口")
public class WarehouseController {

    @Resource
    private WarehouseService warehouseService;

    @GetMapping("/getWarehouseList")
    @ApiOperation(value = "获取仓库列表", notes = "获取仓库列表")
    public AjaxResult<List<Warehouse>> getWarehouseList() {
        List<Warehouse> warehouseList = warehouseService.list(new LambdaQueryWrapper<Warehouse>()
                .select(Warehouse::getId, Warehouse::getCode, Warehouse::getName)
                .eq(Warehouse::getStatus, Status.Enable)
                .orderByDesc(Warehouse::getCreateTime));
        return AjaxResult.success(warehouseList);
    }

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询仓库信息", notes = "根据查询条件分页查询仓库信息")
    public AjaxResult<PageResult<WarehouseVO>> searchByPage(@RequestBody WarehouseQueryDTO param) {
        return AjaxResult.success(PageResult.convert(warehouseService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取仓库信息", notes = "根据仓库ID获取详细信息")
    public AjaxResult<WarehouseVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(warehouseService.getInfo(id));
    }

    @PostMapping("/addWarehouse")
    @ApiOperation(value = "添加仓库信息", notes = "添加仓库信息")
    public AjaxResult<Void> addWarehouse(@RequestBody WarehouseAddDTO param) {
        warehouseService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editWarehouse")
    @ApiOperation(value = "编辑仓库信息", notes = "根据仓库ID编辑仓库信息")
    public AjaxResult<Void> editWarehouse(@RequestBody WarehouseEditDTO param) {
        warehouseService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeWarehouse")
    @ApiOperation(value = "删除仓库信息", notes = "根据仓库ID数组删除仓库信息")
    public AjaxResult<Void> removeWarehouse(@RequestBody String[] ids) {
        warehouseService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/getComboBox")
    @ApiOperation(value = "获取仓库下拉框", notes = "获取仓库下拉框")
    public AjaxResult<List<ComboBox>> getComboBox() {
        List<ComboBox> comboBoxList = warehouseService.list(new LambdaQueryWrapper<Warehouse>()
                        .select(Warehouse::getId, Warehouse::getName)
                        .eq(Warehouse::getStatus, Status.Enable)
                        .orderByDesc(Warehouse::getCreateTime)).stream()
                .map(warehouse -> ComboBoxUtils.convert(warehouse, Warehouse::getId, Warehouse::getName))
                .collect(Collectors.toList());
        return AjaxResult.success(comboBoxList);
    }

}
