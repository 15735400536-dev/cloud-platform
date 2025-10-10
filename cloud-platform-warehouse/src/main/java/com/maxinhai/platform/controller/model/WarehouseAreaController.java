package com.maxinhai.platform.controller.model;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.model.WarehouseAreaAddDTO;
import com.maxinhai.platform.dto.model.WarehouseAreaEditDTO;
import com.maxinhai.platform.dto.model.WarehouseAreaQueryDTO;
import com.maxinhai.platform.po.ComboBox;
import com.maxinhai.platform.enums.Status;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.service.model.WarehouseAreaService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ComboBoxUtils;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.model.WarehouseAreaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/warehouse/area")
@Api(tags = "仓库库区管理接口")
public class WarehouseAreaController {

    @Resource
    private WarehouseAreaService areaService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询库区信息", notes = "根据查询条件分页查询库区信息")
    public AjaxResult<PageResult<WarehouseAreaVO>> searchByPage(@RequestBody WarehouseAreaQueryDTO param) {
        return AjaxResult.success(PageResult.convert(areaService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取库区信息", notes = "根据库区ID获取详细信息")
    public AjaxResult<WarehouseAreaVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(areaService.getInfo(id));
    }

    @PostMapping("/addWarehouseArea")
    @ApiOperation(value = "添加库区信息", notes = "添加库区信息")
    public AjaxResult<Void> addWarehouseArea(@RequestBody WarehouseAreaAddDTO param) {
        areaService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editWarehouseArea")
    @ApiOperation(value = "编辑库区信息", notes = "根据库区ID编辑库区信息")
    public AjaxResult<Void> editWarehouseArea(@RequestBody WarehouseAreaEditDTO param) {
        areaService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeWarehouseArea")
    @ApiOperation(value = "删除库区信息", notes = "根据库区ID数组删除库区信息")
    public AjaxResult<Void> removeWarehouseArea(@RequestBody String[] ids) {
        areaService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/getComboBox/{warehouseId}")
    @ApiOperation(value = "获取库区下拉框", notes = "获取库区下拉框")
    public AjaxResult<List<ComboBox>> getComboBox(@PathVariable(required = false, value = "warehouseId") String warehouseId) {
        List<ComboBox> comboBoxList = areaService.list(new LambdaQueryWrapper<WarehouseArea>()
                        .select(WarehouseArea::getId, WarehouseArea::getName)
                        .eq(StrUtil.isNotBlank(warehouseId), WarehouseArea::getWarehouseId, warehouseId)
                        .eq(WarehouseArea::getStatus, Status.Enable)
                        .orderByDesc(WarehouseArea::getCreateTime)).stream()
                .map(area -> ComboBoxUtils.convert(area, WarehouseArea::getId, WarehouseArea::getName))
                .collect(Collectors.toList());
        return AjaxResult.success(comboBoxList);
    }

}
