package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.SupplierAddDTO;
import com.maxinhai.platform.dto.SupplierEditDTO;
import com.maxinhai.platform.dto.SupplierQueryDTO;
import com.maxinhai.platform.service.SupplierService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.SupplierVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RefreshScope
@RestController
@RequestMapping("/supplier")
@Api(tags = "供应商管理接口")
public class SupplierController {

    @Resource
    private SupplierService supplierService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询供应商信息", notes = "根据查询条件分页查询供应商信息")
    public AjaxResult<PageResult<SupplierVO>> searchByPage(@RequestBody SupplierQueryDTO param) {
        return AjaxResult.success(PageResult.convert(supplierService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取供应商信息", notes = "根据供应商ID获取详细信息")
    public AjaxResult<SupplierVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(supplierService.getInfo(id));
    }

    @PostMapping("/addSupplier")
    @ApiOperation(value = "添加供应商信息", notes = "添加供应商信息")
    public AjaxResult<Void> addOperation(@RequestBody SupplierAddDTO param) {
        supplierService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editSupplier")
    @ApiOperation(value = "编辑供应商信息", notes = "根据供应商ID编辑供应商信息")
    public AjaxResult<Void> editOperation(@RequestBody SupplierEditDTO param) {
        supplierService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeSupplier")
    @ApiOperation(value = "删除供应商信息", notes = "根据供应商ID数组删除供应商信息")
    public AjaxResult<Void> removeOperation(@RequestBody String[] ids) {
        supplierService.remove(ids);
        return AjaxResult.success();
    }

}
