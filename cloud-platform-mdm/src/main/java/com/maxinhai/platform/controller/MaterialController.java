package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.MaterialAddDTO;
import com.maxinhai.platform.dto.MaterialEditDTO;
import com.maxinhai.platform.dto.MaterialQueryDTO;
import com.maxinhai.platform.service.MaterialService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.MaterialVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

@RefreshScope
@RestController
@RequestMapping("/material")
@Api(tags = "物料管理接口")
public class MaterialController {

    @Resource
    private MaterialService materialService;
    private static final String TEMPLATE_PATH = "material.xlsx";

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询物料信息", notes = "根据查询条件分页查询物料信息")
    public AjaxResult<PageResult<MaterialVO>> searchByPage(@RequestBody MaterialQueryDTO param) {
        return AjaxResult.success(PageResult.convert(materialService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取物料信息", notes = "根据物料ID获取详细信息")
    public AjaxResult<MaterialVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(materialService.getInfo(id));
    }

    @PostMapping("/addMaterial")
    @ApiOperation(value = "添加物料信息", notes = "添加物料信息")
    public AjaxResult<Void> addMaterial(@RequestBody MaterialAddDTO param) {
        materialService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editMaterial")
    @ApiOperation(value = "编辑物料信息", notes = "根据物料ID编辑物料信息")
    public AjaxResult<Void> editMaterial(@RequestBody MaterialEditDTO param) {
        materialService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeMaterial")
    @ApiOperation(value = "删除物料信息", notes = "根据物料ID数组删除物料信息")
    public AjaxResult<Void> removeMaterial(@RequestBody String[] ids) {
        materialService.remove(ids);
        return AjaxResult.success();
    }

    @PostMapping("/importExcel")
    @ApiOperation(value = "导入物料数据", notes = "根据Excel模板导入物料数据")
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
        materialService.importExcel(file);
        return AjaxResult.success("Excel数据导入成功！");
    }

    @GetMapping("/downloadTemplate")
    @ApiOperation(value = "下载物料导入模板", notes = "下载物料导入模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        // 1. 从 resources 目录读取文件
        ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH);
        InputStream inputStream = resource.getInputStream();

        // 2. 设置响应头（浏览器下载必备）
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 防止中文文件名乱码
        String fileName = URLEncoder.encode("导入模板.xlsx", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        // 3. 写出文件到浏览器
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        // 4. 关闭流
        inputStream.close();
        outputStream.close();
    }

}
