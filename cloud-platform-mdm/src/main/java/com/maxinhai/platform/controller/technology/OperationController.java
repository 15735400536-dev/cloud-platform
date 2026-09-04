package com.maxinhai.platform.controller.technology;

import com.maxinhai.platform.dto.technology.OperationAddDTO;
import com.maxinhai.platform.dto.technology.OperationEditDTO;
import com.maxinhai.platform.dto.technology.OperationQueryDTO;
import com.maxinhai.platform.service.technology.OperationService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.technology.OperationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

@RefreshScope
@RestController
@RequestMapping("/operation")
@Api(tags = "工序管理接口")
public class OperationController {

    @Resource
    private OperationService operationService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询工序信息", notes = "根据查询条件分页查询工序信息")
    public AjaxResult<PageResult<OperationVO>> searchByPage(@RequestBody OperationQueryDTO param) {
        return AjaxResult.success(PageResult.convert(operationService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取工序信息", notes = "根据工序ID获取详细信息")
    public AjaxResult<OperationVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(operationService.getInfo(id));
    }

    @PostMapping("/addOperation")
    @ApiOperation(value = "添加工序信息", notes = "添加工序信息")
    public AjaxResult<Void> addOperation(@RequestBody OperationAddDTO param) {
        operationService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editOperation")
    @ApiOperation(value = "编辑工序信息", notes = "根据工序ID编辑工序信息")
    public AjaxResult<Void> editOperation(@RequestBody OperationEditDTO param) {
        operationService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeOperation")
    @ApiOperation(value = "删除工序信息", notes = "根据工序ID数组删除工序信息")
    public AjaxResult<Void> removeOperation(@RequestBody String[] ids) {
        operationService.remove(ids);
        return AjaxResult.success();
    }

    @PostMapping("/importExcel")
    @ApiOperation(value = "导入工序数据", notes = "根据Excel模板导入工序数据")
    public AjaxResult<Void> importExcel(@RequestParam("file") MultipartFile file) {
        // 验证文件是否为空
        if (Objects.isNull(file) || file.isEmpty()) {
            return AjaxResult.fail("请选择要上传的Excel文件！");
        }

        // 验证文件格式
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return AjaxResult.fail("请上传Excel格式的文件（.xlsx或.xls）");
        }

        // 调用服务进行导入
        operationService.importExcel(file);
        return AjaxResult.success("Excel数据导入成功！");
    }

}
