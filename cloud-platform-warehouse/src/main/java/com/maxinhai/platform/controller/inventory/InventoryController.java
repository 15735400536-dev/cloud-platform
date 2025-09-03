package com.maxinhai.platform.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.inventory.InventoryQueryDTO;
import com.maxinhai.platform.service.inventory.InventoryService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.inventory.InventoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inventory")
@Api(tags = "库存管理接口")
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询库存信息", notes = "根据查询条件分页查询库存信息")
    public AjaxResult<PageResult<InventoryVO>> searchByPage(@RequestBody InventoryQueryDTO param) {
        return AjaxResult.success(PageResult.convert(inventoryService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取库存信息", notes = "根据库存ID获取详细信息")
    public AjaxResult<InventoryVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(inventoryService.getInfo(id));
    }

}
