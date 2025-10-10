package com.maxinhai.platform.controller.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.model.FactoryAddDTO;
import com.maxinhai.platform.dto.model.FactoryEditDTO;
import com.maxinhai.platform.dto.model.FactoryQueryDTO;
import com.maxinhai.platform.service.model.FactoryService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.model.FactoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

}
