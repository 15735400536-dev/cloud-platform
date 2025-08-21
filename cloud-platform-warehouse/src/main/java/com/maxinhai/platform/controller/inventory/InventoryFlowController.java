package com.maxinhai.platform.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.inventory.InventoryFlowQueryDTO;
import com.maxinhai.platform.service.inventory.InventoryFlowService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.inventory.InventoryFlowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：InventoryFlowController
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 10:11
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@RestController
@RequestMapping("/inventoryFlow")
@Api(tags = "库存流水管理接口")
public class InventoryFlowController {

    @Resource
    private InventoryFlowService inventoryFlowService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询库存流水信息", notes = "根据查询条件分页查询库存流水信息")
    public AjaxResult<Page<InventoryFlowVO>> searchByPage(@RequestBody InventoryFlowQueryDTO param) {
        return AjaxResult.success(PageResult.convert(inventoryFlowService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取库存流水信息", notes = "根据库存流水ID获取详细信息")
    public AjaxResult<InventoryFlowVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(inventoryFlowService.getInfo(id));
    }

}
