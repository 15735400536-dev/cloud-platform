package com.maxinhai.platform.controller.technology;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.ProductAddDTO;
import com.maxinhai.platform.dto.ProductEditDTO;
import com.maxinhai.platform.dto.ProductQueryDTO;
import com.maxinhai.platform.service.ProductService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.ProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product")
@Api(tags = "产品管理接口")
public class ProductController {

    @Resource
    private ProductService productService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询产品信息", notes = "根据查询条件分页查询产品信息")
    public AjaxResult<PageResult<ProductVO>> searchByPage(@RequestBody ProductQueryDTO param) {
        return AjaxResult.success(PageResult.convert(productService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取产品信息", notes = "根据产品ID获取详细信息")
    public AjaxResult<ProductVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(productService.getInfo(id));
    }

    @PostMapping("/addProduct")
    @ApiOperation(value = "添加产品信息", notes = "添加产品信息")
    public AjaxResult<Void> addProduct(@RequestBody ProductAddDTO param) {
        productService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editProduct")
    @ApiOperation(value = "编辑产品信息", notes = "根据产品ID编辑产品信息")
    public AjaxResult<Void> editProduct(@RequestBody ProductEditDTO param) {
        productService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeProduct")
    @ApiOperation(value = "删除产品信息", notes = "根据产品ID数组删除产品信息")
    public AjaxResult<Void> removeProduct(@RequestBody String[] ids) {
        productService.remove(ids);
        return AjaxResult.success();
    }

}
