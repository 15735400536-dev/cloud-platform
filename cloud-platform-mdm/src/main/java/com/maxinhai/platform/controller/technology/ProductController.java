package com.maxinhai.platform.controller.technology;

import com.maxinhai.platform.dto.ProductAddDTO;
import com.maxinhai.platform.dto.ProductEditDTO;
import com.maxinhai.platform.dto.ProductQueryDTO;
import com.maxinhai.platform.service.technology.ProductService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.ProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/importExcel")
    @ApiOperation(value = "导入产品数据", notes = "根据Excel模板导入产品数据")
    public AjaxResult<Void> importExcel(@RequestParam("file") MultipartFile file) {
        // 验证文件是否为空
        if (file.isEmpty()) {
            return AjaxResult.fail("请选择要上传的Excel文件！");
        }

        // 验证文件格式
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return AjaxResult.fail("请上传Excel格式的文件（.xlsx或.xls）");
        }

        // 调用服务进行导入
        productService.importExcel(file);
        return AjaxResult.success("Excel数据导入成功！");
    }

}
