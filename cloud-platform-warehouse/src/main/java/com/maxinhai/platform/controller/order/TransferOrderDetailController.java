package com.maxinhai.platform.controller.order;

import com.maxinhai.platform.dto.order.TransferOrderDetailAddDTO;
import com.maxinhai.platform.dto.order.TransferOrderDetailEditDTO;
import com.maxinhai.platform.dto.order.TransferOrderDetailQueryDTO;
import com.maxinhai.platform.service.order.TransferOrderDetailService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.order.TransferOrderDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/transferOrderDetail")
@Api(tags = "移库单明细管理接口")
public class TransferOrderDetailController {

    @Resource
    private TransferOrderDetailService transferOrderDetailService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询移库单信息", notes = "根据查询条件分页查询移库单信息")
    public AjaxResult<PageResult<TransferOrderDetailVO>> searchByPage(@RequestBody TransferOrderDetailQueryDTO param) {
        return AjaxResult.success(PageResult.convert(transferOrderDetailService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取移库单信息", notes = "根据移库单ID获取详细信息")
    public AjaxResult<TransferOrderDetailVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(transferOrderDetailService.getInfo(id));
    }

    @PostMapping("/addTransferOrder")
    @ApiOperation(value = "添加移库单信息", notes = "添加移库单信息")
    public AjaxResult<Void> addTransferOrder(@RequestBody TransferOrderDetailAddDTO param) {
        transferOrderDetailService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editTransferOrder")
    @ApiOperation(value = "编辑移库单信息", notes = "根据移库单ID编辑移库单信息")
    public AjaxResult<Void> editTransferOrder(@RequestBody TransferOrderDetailEditDTO param) {
        transferOrderDetailService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeTransferOrder")
    @ApiOperation(value = "删除移库单信息", notes = "根据移库单ID数组删除移库单信息")
    public AjaxResult<Void> removeTransferOrder(@RequestBody String[] ids) {
        transferOrderDetailService.remove(ids);
        return AjaxResult.success();
    }

}
