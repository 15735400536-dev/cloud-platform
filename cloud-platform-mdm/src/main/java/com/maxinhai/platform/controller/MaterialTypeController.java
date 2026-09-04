package com.maxinhai.platform.controller;

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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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

    @PostMapping("/importExcel")
    @ApiOperation(value = "导入物料类型", notes = "根据excel模板导入物料类型")
    public AjaxResult<Void> importExcel(@RequestParam("file") MultipartFile file) {
        // 验证文件是否为空
        if (Objects.isNull(file) || file.isEmpty()) {
            return AjaxResult.fail("请选择要上传的Excel文件！");
        }

        // 验证文件格式
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return AjaxResult.fail("请上传Excel格式的文件（.xlsx或.xls）");
        }

        // 调用服务进行导入
        materialTypeService.importExcel(file);
        return AjaxResult.success("Excel数据导入成功！");
    }

}
