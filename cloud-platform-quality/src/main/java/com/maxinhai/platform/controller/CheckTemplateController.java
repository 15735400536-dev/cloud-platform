package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.CheckTemplateAddDTO;
import com.maxinhai.platform.dto.CheckTemplateEditDTO;
import com.maxinhai.platform.dto.CheckTemplateQueryDTO;
import com.maxinhai.platform.service.CheckTemplateService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CheckTemplateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/checkTemplate")
@Api(tags = "检测模板管理接口")
public class CheckTemplateController {

    @Resource
    private CheckTemplateService checkTemplateService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询检测模板信息", notes = "根据查询条件分页查询检测模板信息")
    public AjaxResult<PageResult<CheckTemplateVO>> searchByPage(@RequestBody CheckTemplateQueryDTO param) {
        return AjaxResult.success(PageResult.convert(checkTemplateService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取检测模板信息", notes = "根据检测模板ID获取详细信息")
    public AjaxResult<CheckTemplateVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(checkTemplateService.getInfo(id));
    }

    @PostMapping("/addCheckTemplate")
    @ApiOperation(value = "添加检测模板信息", notes = "添加检测模板信息")
    public AjaxResult<Void> addCheckTemplate(@RequestBody CheckTemplateAddDTO param) {
        checkTemplateService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCheckTemplate")
    @ApiOperation(value = "编辑检测模板信息", notes = "根据检测模板ID编辑检测模板信息")
    public AjaxResult<Void> editCheckTemplate(@RequestBody CheckTemplateEditDTO param) {
        checkTemplateService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCheckTemplate")
    @ApiOperation(value = "删除检测模板信息", notes = "根据检测模板ID数组删除检测模板信息")
    public AjaxResult<Void> removeCheckTemplate(@RequestBody String[] ids) {
        checkTemplateService.remove(ids);
        return AjaxResult.success();
    }

    @PostMapping("/importExcel")
    @ApiOperation(value = "导入检测模板", notes = "根据Excel模板导入检测模板")
    public AjaxResult<Void> importExcel(@RequestParam("file") MultipartFile file) {
        // 验证文件是否为空
        if (file.isEmpty()) {
            return AjaxResult.fail("请选择要上传的Excel文件！");
        }

        // 验证文件格式
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return AjaxResult.fail("请上传Excel格式的文件（.xlsx或.xls）");
        }

        // 调用服务进行导入
        checkTemplateService.importExcel(file);
        return AjaxResult.success("Excel数据导入成功！");
    }

}
