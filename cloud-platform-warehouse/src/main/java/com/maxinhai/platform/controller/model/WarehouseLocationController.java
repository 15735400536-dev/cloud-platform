package com.maxinhai.platform.controller.model;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.model.WarehouseLocationAddDTO;
import com.maxinhai.platform.dto.model.WarehouseLocationEditDTO;
import com.maxinhai.platform.dto.model.WarehouseLocationQueryDTO;
import com.maxinhai.platform.po.ComboBox;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.service.model.WarehouseLocationService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ComboBoxUtils;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.model.WarehouseLocationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/warehouse/location")
@Api(tags = "仓库货位管理接口")
public class WarehouseLocationController {

    @Resource
    private WarehouseLocationService locationService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询货位信息", notes = "根据查询条件分页查询货位信息")
    public AjaxResult<PageResult<WarehouseLocationVO>> searchByPage(@RequestBody WarehouseLocationQueryDTO param) {
        return AjaxResult.success(PageResult.convert(locationService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取货位信息", notes = "根据货位ID获取详细信息")
    public AjaxResult<WarehouseLocationVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(locationService.getInfo(id));
    }

    @PostMapping("/addWarehouseLocation")
    @ApiOperation(value = "添加货位信息", notes = "添加货位信息")
    public AjaxResult<Void> addWarehouseLocation(@RequestBody WarehouseLocationAddDTO param) {
        locationService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editWarehouseLocation")
    @ApiOperation(value = "编辑货位信息", notes = "根据货位ID编辑货位信息")
    public AjaxResult<Void> editWarehouseLocation(@RequestBody WarehouseLocationEditDTO param) {
        locationService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeWarehouseLocation")
    @ApiOperation(value = "删除货位信息", notes = "根据货位ID数组删除货位信息")
    public AjaxResult<Void> removeWarehouseLocation(@RequestBody String[] ids) {
        locationService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/getComboBox/{rackId}")
    @ApiOperation(value = "获取货位下拉框", notes = "获取货位下拉框")
    public AjaxResult<List<ComboBox>> getComboBox(@PathVariable(required = false, value = "rackId") String rackId) {
        List<ComboBox> comboBoxList = locationService.list(new LambdaQueryWrapper<WarehouseLocation>()
                        .select(WarehouseLocation::getId, WarehouseLocation::getName)
                        .eq(StrUtil.isNotBlank(rackId), WarehouseLocation::getRackId, rackId)
                        .orderByDesc(WarehouseLocation::getCreateTime)).stream()
                .map(location -> ComboBoxUtils.convert(location, WarehouseLocation::getId, WarehouseLocation::getName))
                .collect(Collectors.toList());
        return AjaxResult.success(comboBoxList);
    }

}
