package com.maxinhai.platform.controller.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.order.TransferOrderAddDTO;
import com.maxinhai.platform.dto.order.TransferOrderEditDTO;
import com.maxinhai.platform.dto.order.TransferOrderQueryDTO;
import com.maxinhai.platform.service.order.TransferOrderService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.order.TransferOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/transferOrder")
@Api(tags = "移库单管理接口")
public class TransferOrderController {

    @Resource
    private TransferOrderService transferOrderService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询移库单信息", notes = "根据查询条件分页查询移库单信息")
    public AjaxResult<Page<TransferOrderVO>> searchByPage(@RequestBody TransferOrderQueryDTO param) {
        return AjaxResult.success(PageResult.convert(transferOrderService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取移库单信息", notes = "根据移库单ID获取详细信息")
    public AjaxResult<TransferOrderVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(transferOrderService.getInfo(id));
    }

    @PostMapping("/addTransferOrder")
    @ApiOperation(value = "添加移库单信息", notes = "添加移库单信息")
    public AjaxResult<Void> addTransferOrder(@RequestBody TransferOrderAddDTO param) {
        transferOrderService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editTransferOrder")
    @ApiOperation(value = "编辑移库单信息", notes = "根据移库单ID编辑移库单信息")
    public AjaxResult<Void> editTransferOrder(@RequestBody TransferOrderEditDTO param) {
        transferOrderService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeTransferOrder")
    @ApiOperation(value = "删除移库单信息", notes = "根据移库单ID数组删除移库单信息")
    public AjaxResult<Void> removeTransferOrder(@RequestBody String[] ids) {
        transferOrderService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/transfer/{orderId}")
    @ApiOperation(value = "根据移库单ID移库", notes = "根据移库单ID移库")
    public AjaxResult<Void> transfer(@PathVariable("orderId") String orderId) {
        transferOrderService.transfer(orderId);
        return AjaxResult.success();
    }
}
