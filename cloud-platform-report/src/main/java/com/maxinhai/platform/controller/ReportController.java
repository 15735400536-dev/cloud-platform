package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.CustomReportAddDTO;
import com.maxinhai.platform.dto.CustomReportEditDTO;
import com.maxinhai.platform.dto.CustomReportQueryDTO;
import com.maxinhai.platform.service.CustomReportService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CustomReportPreviewVO;
import com.maxinhai.platform.vo.CustomReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：ConditionController
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:40
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@RestController
@RequestMapping("/report")
@Api(tags = "报表管理接口")
public class ReportController {

    @Resource
    private CustomReportService reportService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询报表信息", notes = "根据查询条件分页查询报表信息")
    public AjaxResult<Page<CustomReportVO>> searchByPage(@RequestBody CustomReportQueryDTO param) {
        return AjaxResult.success(PageResult.convert(reportService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取报表信息", notes = "根据报表ID获取详细信息")
    public AjaxResult<CustomReportVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(reportService.getInfo(id));
    }

    @PostMapping("/addCustomReport")
    @ApiOperation(value = "添加报表信息", notes = "添加报表信息")
    public AjaxResult<Void> addCustomReport(@RequestBody CustomReportAddDTO param) {
        reportService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCustomReport")
    @ApiOperation(value = "编辑报表信息", notes = "根据报表ID编辑报表信息")
    public AjaxResult<Void> editCustomReport(@RequestBody CustomReportEditDTO param) {
        reportService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCustomReport")
    @ApiOperation(value = "删除报表信息", notes = "根据报表ID数组删除报表信息")
    public AjaxResult<Void> removeCustomReport(@RequestBody String[] ids) {
        reportService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/preview/{reportId}")
    @ApiOperation(value = "根据报表ID预览报表", notes = "根据报表ID预览报表")
    public AjaxResult<CustomReportPreviewVO> preview(@PathVariable("reportId") String reportId) {
        return AjaxResult.success(reportService.preview(reportId));
    }

}
