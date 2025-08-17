package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

import javax.annotation.Resource;

@RestController
@RequestMapping("/checkTemplate")
@Api(tags = "检测模板管理接口")
public class CheckTemplateController {

    @Resource
    private CheckTemplateService checkTemplateService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询检测模板信息", notes = "根据查询条件分页查询检测模板信息")
    public AjaxResult<Page<CheckTemplateVO>> searchByPage(@RequestBody CheckTemplateQueryDTO param) {
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

}
