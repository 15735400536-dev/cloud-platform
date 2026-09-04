package com.maxinhai.platform.controller.order;

import com.maxinhai.platform.dto.order.ReceiptOrderDetailAddDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderDetailEditDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderDetailQueryDTO;
import com.maxinhai.platform.service.order.ReceiptOrderDetailService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.order.ReceiptOrderDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/receiptOrderDetail")
@Api(tags = "入库单明细管理接口")
public class ReceiptOrderDetailController {

    @Resource
    private ReceiptOrderDetailService receiptOrderDetailService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询入库单信息", notes = "根据查询条件分页查询入库单信息")
    public AjaxResult<PageResult<ReceiptOrderDetailVO>> searchByPage(@RequestBody ReceiptOrderDetailQueryDTO param) {
        return AjaxResult.success(PageResult.convert(receiptOrderDetailService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取入库单信息", notes = "根据入库单ID获取详细信息")
    public AjaxResult<ReceiptOrderDetailVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(receiptOrderDetailService.getInfo(id));
    }

    @PostMapping("/addReceiptOrder")
    @ApiOperation(value = "添加入库单信息", notes = "添加入库单信息")
    public AjaxResult<Void> addReceiptOrder(@RequestBody ReceiptOrderDetailAddDTO param) {
        receiptOrderDetailService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editReceiptOrder")
    @ApiOperation(value = "编辑入库单信息", notes = "根据入库单ID编辑入库单信息")
    public AjaxResult<Void> editReceiptOrder(@RequestBody ReceiptOrderDetailEditDTO param) {
        receiptOrderDetailService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeReceiptOrder")
    @ApiOperation(value = "删除入库单信息", notes = "根据入库单ID数组删除入库单信息")
    public AjaxResult<Void> removeReceiptOrder(@RequestBody String[] ids) {
        receiptOrderDetailService.remove(ids);
        return AjaxResult.success();
    }

}
