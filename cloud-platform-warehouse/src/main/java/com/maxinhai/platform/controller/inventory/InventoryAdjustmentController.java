package com.maxinhai.platform.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.inventory.InventoryAdjustmentQueryDTO;
import com.maxinhai.platform.service.inventory.InventoryAdjustmentService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.inventory.InventoryAdjustmentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inventoryAdjustment")
@Api(tags = "库存调整单管理接口")
public class InventoryAdjustmentController {

    @Resource
    private InventoryAdjustmentService inventoryAdjustmentService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询库存调整单信息", notes = "根据查询条件分页查询库存调整单信息")
    public AjaxResult<PageResult<InventoryAdjustmentVO>> searchByPage(@RequestBody InventoryAdjustmentQueryDTO param) {
        return AjaxResult.success(PageResult.convert(inventoryAdjustmentService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取库存调整单信息", notes = "根据库存调整单ID获取详细信息")
    public AjaxResult<InventoryAdjustmentVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(inventoryAdjustmentService.getInfo(id));
    }

}
