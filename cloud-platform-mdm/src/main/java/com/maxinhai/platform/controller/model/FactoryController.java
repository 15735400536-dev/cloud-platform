package com.maxinhai.platform.controller.model;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.dto.model.FactoryAddDTO;
import com.maxinhai.platform.dto.model.FactoryEditDTO;
import com.maxinhai.platform.dto.model.FactoryQueryDTO;
import com.maxinhai.platform.po.ComboBox;
import com.maxinhai.platform.po.model.Factory;
import com.maxinhai.platform.service.model.FactoryService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ComboBoxUtils;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.model.FactoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/factory")
@Api(tags = "工厂管理接口")
public class FactoryController {

    @Resource
    private FactoryService factoryService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询工厂信息", notes = "根据查询条件分页查询工厂信息")
    public AjaxResult<PageResult<FactoryVO>> searchByPage(@RequestBody FactoryQueryDTO param) {
        return AjaxResult.success(PageResult.convert(factoryService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取工厂信息", notes = "根据工厂ID获取详细信息")
    public AjaxResult<FactoryVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(factoryService.getInfo(id));
    }

    @PostMapping("/addFactory")
    @ApiOperation(value = "添加工厂信息", notes = "添加工厂信息")
    public AjaxResult<Void> addFactory(@RequestBody FactoryAddDTO param) {
        factoryService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editFactory")
    @ApiOperation(value = "编辑工厂信息", notes = "根据工厂ID编辑工厂信息")
    public AjaxResult<Void> editFactory(@RequestBody FactoryEditDTO param) {
        factoryService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeFactory")
    @ApiOperation(value = "删除工厂信息", notes = "根据工厂ID数组删除工厂信息")
    public AjaxResult<Void> removeFactory(@RequestBody String[] ids) {
        factoryService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/getComboBox")
    @ApiOperation(value = "获取工厂下拉框", notes = "获取工厂下拉框")
    public AjaxResult<List<ComboBox>> getComboBox() {
        List<Factory> factoryList = factoryService.list(new LambdaQueryWrapper<Factory>()
                .select(Factory::getId, Factory::getName)
                .orderByDesc(Factory::getCreateTime));
        List<ComboBox> comboBoxList = factoryList.stream().map(factory -> {
            return ComboBoxUtils.convert(factory, Factory::getId, Factory::getName);
        }).collect(Collectors.toList());
        return AjaxResult.success(comboBoxList);
    }

}
