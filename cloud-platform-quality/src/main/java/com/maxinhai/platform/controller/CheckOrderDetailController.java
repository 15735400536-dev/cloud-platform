package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.CheckOrderDetailAddDTO;
import com.maxinhai.platform.dto.CheckOrderDetailEditDTO;
import com.maxinhai.platform.dto.CheckOrderDetailQueryDTO;
import com.maxinhai.platform.service.CheckOrderDetailService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CheckOrderDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/checkOrderDetail")
@Api(tags = "检测单明细管理接口")
public class CheckOrderDetailController {

    @Resource
    private CheckOrderDetailService checkOrderDetailService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询检测单明细信息", notes = "根据查询条件分页查询检测单明细信息")
    public AjaxResult<Page<CheckOrderDetailVO>> searchByPage(@RequestBody CheckOrderDetailQueryDTO param) {
        return AjaxResult.success(PageResult.convert(checkOrderDetailService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取检测单明细信息", notes = "根据检测单明细ID获取详细信息")
    public AjaxResult<CheckOrderDetailVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(checkOrderDetailService.getInfo(id));
    }

    @PostMapping("/addCheckOrderDetail")
    @ApiOperation(value = "添加检测单明细信息", notes = "添加检测单明细信息")
    public AjaxResult<Void> addCheckOrderDetail(@RequestBody CheckOrderDetailAddDTO param) {
        checkOrderDetailService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCheckOrderDetail")
    @ApiOperation(value = "编辑检测单明细信息", notes = "根据检测单明细ID编辑检测单明细信息")
    public AjaxResult<Void> editCheckOrderDetail(@RequestBody CheckOrderDetailEditDTO param) {
        checkOrderDetailService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCheckOrderDetail")
    @ApiOperation(value = "删除检测单明细信息", notes = "根据检测单明细ID数组删除检测单明细信息")
    public AjaxResult<Void> removeCheckOrderDetail(@RequestBody String[] ids) {
        checkOrderDetailService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/getCheckItemList/{checkOrderId}")
    @ApiOperation(value = "根据质检单ID查询检测项列表", notes = "根据质检单ID查询检测项列表")
    public AjaxResult<CheckOrderDetailVO> getCheckItemList(@PathVariable("checkOrderId") String checkOrderId) {
        return AjaxResult.success(checkOrderDetailService.getCheckItemList(checkOrderId));
    }

    @PostMapping("/filing")
    @ApiOperation(value = "填写质检单", notes = "填写质检单")
    public AjaxResult<Void> filing(@RequestBody List<CheckOrderDetailEditDTO> itemList) {
        checkOrderDetailService.filing(itemList);
        return AjaxResult.success();
    }
}
