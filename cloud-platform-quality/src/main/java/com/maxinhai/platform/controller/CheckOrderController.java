package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.CheckOrderAddDTO;
import com.maxinhai.platform.dto.CheckOrderEditDTO;
import com.maxinhai.platform.dto.CheckOrderQueryDTO;
import com.maxinhai.platform.service.CheckOrderService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CheckOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/checkOrder")
@Api(tags = "检测单管理接口")
public class CheckOrderController {

    @Resource
    private CheckOrderService checkOrderService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询检测单信息", notes = "根据查询条件分页查询检测单信息")
    public AjaxResult<Page<CheckOrderVO>> searchByPage(@RequestBody CheckOrderQueryDTO param) {
        return AjaxResult.success(PageResult.convert(checkOrderService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取检测单信息", notes = "根据检测单ID获取详细信息")
    public AjaxResult<CheckOrderVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(checkOrderService.getInfo(id));
    }

    @PostMapping("/addCheckOrder")
    @ApiOperation(value = "添加检测单信息", notes = "添加检测单信息")
    public AjaxResult<Void> addCheckOrder(@RequestBody CheckOrderAddDTO param) {
        checkOrderService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCheckOrder")
    @ApiOperation(value = "编辑检测单信息", notes = "根据检测单ID编辑检测单信息")
    public AjaxResult<Void> editCheckOrder(@RequestBody CheckOrderEditDTO param) {
        checkOrderService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCheckOrder")
    @ApiOperation(value = "删除检测单信息", notes = "根据检测单ID数组删除检测单信息")
    public AjaxResult<Void> removeCheckOrder(@RequestBody String[] ids) {
        checkOrderService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/generate/{workOrderId}")
    @ApiOperation(value = "生成质检单", notes = "根据工单ID生成质检单")
    public AjaxResult<Void> generate(@PathVariable("workOrderId") String workOrderId) {
        checkOrderService.generate(workOrderId);
        return AjaxResult.success();
    }

    @GetMapping("/generateTaskCheckOrder/{taskOrderId}")
    @ApiOperation(value = "生成工序质检单", notes = "根据派工单ID生成质检单")
    public AjaxResult<Void> generateTaskCheckOrder(@PathVariable("taskOrderId") String taskOrderId) {
        checkOrderService.generateCheckOrder(taskOrderId);
        return AjaxResult.success();
    }

}
