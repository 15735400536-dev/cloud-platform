package com.maxinhai.platform.controller.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.order.IssueOrderAddDTO;
import com.maxinhai.platform.dto.order.IssueOrderEditDTO;
import com.maxinhai.platform.dto.order.IssueOrderQueryDTO;
import com.maxinhai.platform.service.order.IssueOrderService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.order.IssueOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/issueOrder")
@Api(tags = "出库单管理接口")
public class IssueOrderController {

    @Resource
    private IssueOrderService issueOrderService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询出库单信息", notes = "根据查询条件分页查询出库单信息")
    public AjaxResult<Page<IssueOrderVO>> searchByPage(@RequestBody IssueOrderQueryDTO param) {
        return AjaxResult.success(PageResult.convert(issueOrderService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取出库单信息", notes = "根据出库单ID获取详细信息")
    public AjaxResult<IssueOrderVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(issueOrderService.getInfo(id));
    }

    @PostMapping("/addIssueOrder")
    @ApiOperation(value = "添加出库单信息", notes = "添加出库单信息")
    public AjaxResult<Void> addIssueOrder(@RequestBody IssueOrderAddDTO param) {
        issueOrderService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editIssueOrder")
    @ApiOperation(value = "编辑出库单信息", notes = "根据出库单ID编辑出库单信息")
    public AjaxResult<Void> editIssueOrder(@RequestBody IssueOrderEditDTO param) {
        issueOrderService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeIssueOrder")
    @ApiOperation(value = "删除出库单信息", notes = "根据出库单ID数组删除出库单信息")
    public AjaxResult<Void> removeIssueOrder(@RequestBody String[] ids) {
        issueOrderService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/issue/{orderId}")
    @ApiOperation(value = "根据出库单ID出库", notes = "根据出库单ID出库")
    public AjaxResult<Void> issue(@PathVariable("orderId") String orderId) {
        issueOrderService.issue(orderId);
        return AjaxResult.success();
    }
}
