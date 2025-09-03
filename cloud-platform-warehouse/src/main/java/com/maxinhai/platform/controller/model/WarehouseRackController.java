package com.maxinhai.platform.controller.model;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.model.WarehouseRackAddDTO;
import com.maxinhai.platform.dto.model.WarehouseRackEditDTO;
import com.maxinhai.platform.dto.model.WarehouseRackQueryDTO;
import com.maxinhai.platform.po.ComboBox;
import com.maxinhai.platform.po.model.WarehouseRack;
import com.maxinhai.platform.service.model.WarehouseRackService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ComboBoxUtils;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.model.WarehouseRackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/warehouse/rack")
@Api(tags = "仓库货架管理接口")
public class WarehouseRackController {

    @Resource
    private WarehouseRackService rackService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询货架信息", notes = "根据查询条件分页查询货架信息")
    public AjaxResult<PageResult<WarehouseRackVO>> searchByPage(@RequestBody WarehouseRackQueryDTO param) {
        return AjaxResult.success(PageResult.convert(rackService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取货架信息", notes = "根据货架ID获取详细信息")
    public AjaxResult<WarehouseRackVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(rackService.getInfo(id));
    }

    @PostMapping("/addWarehouseRack")
    @ApiOperation(value = "添加货架信息", notes = "添加货架信息")
    public AjaxResult<Void> addWarehouseRack(@RequestBody WarehouseRackAddDTO param) {
        rackService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editWarehouseRack")
    @ApiOperation(value = "编辑货架信息", notes = "根据货架ID编辑货架信息")
    public AjaxResult<Void> editWarehouseRack(@RequestBody WarehouseRackEditDTO param) {
        rackService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeWarehouseRack")
    @ApiOperation(value = "删除货架信息", notes = "根据货架ID数组删除货架信息")
    public AjaxResult<Void> removeWarehouseRack(@RequestBody String[] ids) {
        rackService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/getComboBox/{areaId}")
    @ApiOperation(value = "获取货架下拉框", notes = "获取货架下拉框")
    public AjaxResult<List<ComboBox>> getComboBox(@PathVariable(required = false, value = "areaId") String areaId) {
        List<ComboBox> comboBoxList = rackService.list(new LambdaQueryWrapper<WarehouseRack>()
                        .select(WarehouseRack::getId, WarehouseRack::getName)
                        .eq(StrUtil.isNotBlank(areaId), WarehouseRack::getAreaId, areaId)
                        .orderByDesc(WarehouseRack::getCreateTime)).stream()
                .map(rack -> ComboBoxUtils.convert(rack, WarehouseRack::getId, WarehouseRack::getName))
                .collect(Collectors.toList());
        return AjaxResult.success(comboBoxList);
    }

}
