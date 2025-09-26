package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.DictTypeAddDTO;
import com.maxinhai.platform.dto.DictTypeEditDTO;
import com.maxinhai.platform.dto.DictTypeQueryDTO;
import com.maxinhai.platform.service.DictTypeService;
import com.maxinhai.platform.vo.DictTypeVO;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/dictType")
@Api(tags = "字典类型管理接口")
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询字典类型信息", notes = "根据查询条件分页查询字典类型信息")
    public AjaxResult<PageResult<DictTypeVO>> searchByPage(@RequestBody DictTypeQueryDTO param) {
        return AjaxResult.success(PageResult.convert(dictTypeService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取字典类型信息", notes = "根据字典类型ID获取详细信息")
    public AjaxResult<DictTypeVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(dictTypeService.getInfo(id));
    }

    @PostMapping("/addDictType")
    @ApiOperation(value = "添加字典类型信息", notes = "添加字典类型信息")
    public AjaxResult<Void> addDictType(@RequestBody DictTypeAddDTO param) {
        dictTypeService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editDictType")
    @ApiOperation(value = "编辑字典类型信息", notes = "根据字典类型ID编辑字典类型信息")
    public AjaxResult<Void> editDictType(@RequestBody DictTypeEditDTO param) {
        dictTypeService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeDictType")
    @ApiOperation(value = "删除字典类型信息", notes = "根据字典类型ID数组删除字典类型信息")
    public AjaxResult<Void> removeDictType(@RequestBody String[] ids) {
        dictTypeService.remove(ids);
        return AjaxResult.success();
    }

    @PostMapping("/importExcel")
    @ApiOperation(value = "导入数据字典", notes = "根据Excel模板导入数据字典")
    public AjaxResult<String> importExcel(MultipartFile file) {
        // 验证文件是否为空
        if (Objects.isNull(file) || file.isEmpty()) {
            return AjaxResult.fail("请选择要上传的Excel文件！");
        }

        // 验证文件格式
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return AjaxResult.fail("请上传Excel格式的文件（.xlsx或.xls）");
        }

        dictTypeService.importExcel(file);
        return AjaxResult.success("导入成功!");
    }
}
