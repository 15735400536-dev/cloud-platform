package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.MaterialTypeAddDTO;
import com.maxinhai.platform.dto.MaterialTypeEditDTO;
import com.maxinhai.platform.dto.MaterialTypeQueryDTO;
import com.maxinhai.platform.service.MaterialTypeService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.MaterialTypeTreeVO;
import com.maxinhai.platform.vo.MaterialTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RefreshScope
@RestController
@RequestMapping("/materialType")
@Api(tags = "物料类型管理接口")
public class MaterialTypeController {

    @Resource
    private MaterialTypeService materialTypeService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询物料类型信息", notes = "根据查询条件分页查询物料类型信息")
    public AjaxResult<PageResult<MaterialTypeVO>> searchByPage(@RequestBody MaterialTypeQueryDTO param) {
        return AjaxResult.success(PageResult.convert(materialTypeService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取物料类型信息", notes = "根据物料类型ID获取详细信息")
    public AjaxResult<MaterialTypeVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(materialTypeService.getInfo(id));
    }

    @PostMapping("/addMaterialType")
    @ApiOperation(value = "添加物料类型信息", notes = "添加物料类型信息")
    public AjaxResult<Void> addMaterialType(@RequestBody MaterialTypeAddDTO param) {
        materialTypeService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editMaterialType")
    @ApiOperation(value = "编辑物料类型信息", notes = "根据物料类型ID编辑物料类型信息")
    public AjaxResult<Void> editMaterialType(@RequestBody MaterialTypeEditDTO param) {
        materialTypeService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeMaterialType")
    @ApiOperation(value = "删除物料类型信息", notes = "根据物料类型ID数组删除物料类型信息")
    public AjaxResult<Void> removeMaterialType(@RequestBody String[] ids) {
        materialTypeService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/getTree")
    @ApiOperation(value = "获取物料类型树状结构", notes = "获取物料类型树状结构")
    public AjaxResult<List<MaterialTypeTreeVO>> getTree() {
        return AjaxResult.success(materialTypeService.getMaterialTypeTree());
    }

}
