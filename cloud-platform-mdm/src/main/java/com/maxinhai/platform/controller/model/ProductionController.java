package com.maxinhai.platform.controller.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.model.ProductionLineAddDTO;
import com.maxinhai.platform.dto.model.ProductionLineEditDTO;
import com.maxinhai.platform.dto.model.ProductionLineQueryDTO;
import com.maxinhai.platform.service.model.ProductionLineService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.model.ProductionLineVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/productionLine")
@Api(tags = "产线管理接口")
public class ProductionController {

    @Resource
    private ProductionLineService productionLineService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询产线信息", notes = "根据查询条件分页查询产线信息")
    public AjaxResult<PageResult<ProductionLineVO>> searchByPage(@RequestBody ProductionLineQueryDTO param) {
        return AjaxResult.success(PageResult.convert(productionLineService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取产线信息", notes = "根据产线ID获取详细信息")
    public AjaxResult<ProductionLineVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(productionLineService.getInfo(id));
    }

    @PostMapping("/addProductionLine")
    @ApiOperation(value = "添加产线信息", notes = "添加产线信息")
    public AjaxResult<Void> addProductionLine(@RequestBody ProductionLineAddDTO param) {
        productionLineService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editProductionLine")
    @ApiOperation(value = "编辑产线信息", notes = "根据产线ID编辑产线信息")
    public AjaxResult<Void> editProductionLine(@RequestBody ProductionLineEditDTO param) {
        productionLineService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeProductionLine")
    @ApiOperation(value = "删除产线信息", notes = "根据产线ID数组删除产线信息")
    public AjaxResult<Void> removeProductionLine(@RequestBody String[] ids) {
        productionLineService.remove(ids);
        return AjaxResult.success();
    }

}
