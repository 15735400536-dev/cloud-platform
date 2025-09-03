package com.maxinhai.platform.controller.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.order.IssueOrderDetailAddDTO;
import com.maxinhai.platform.dto.order.IssueOrderDetailEditDTO;
import com.maxinhai.platform.dto.order.IssueOrderDetailQueryDTO;
import com.maxinhai.platform.service.order.IssueOrderDetailService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.order.IssueOrderDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/issueOrderDetail")
@Api(tags = "出库单明细管理接口")
public class IssueOrderDetailController {

    @Resource
    private IssueOrderDetailService issueOrderDetailService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询出库单信息", notes = "根据查询条件分页查询出库单信息")
    public AjaxResult<PageResult<IssueOrderDetailVO>> searchByPage(@RequestBody IssueOrderDetailQueryDTO param) {
        return AjaxResult.success(PageResult.convert(issueOrderDetailService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取出库单信息", notes = "根据出库单ID获取详细信息")
    public AjaxResult<IssueOrderDetailVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(issueOrderDetailService.getInfo(id));
    }

    @PostMapping("/addIssueOrderDetail")
    @ApiOperation(value = "添加出库单信息", notes = "添加出库单信息")
    public AjaxResult<Void> addIssueOrderDetail(@RequestBody IssueOrderDetailAddDTO param) {
        issueOrderDetailService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editIssueOrderDetail")
    @ApiOperation(value = "编辑出库单信息", notes = "根据出库单ID编辑出库单信息")
    public AjaxResult<Void> editIssueOrderDetail(@RequestBody IssueOrderDetailEditDTO param) {
        issueOrderDetailService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeIssueOrderDetail")
    @ApiOperation(value = "删除出库单信息", notes = "根据出库单ID数组删除出库单信息")
    public AjaxResult<Void> removeIssueOrderDetail(@RequestBody String[] ids) {
        issueOrderDetailService.remove(ids);
        return AjaxResult.success();
    }

}
