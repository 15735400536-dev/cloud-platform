package com.maxinhai.platform.controller.technology;

import com.maxinhai.platform.dto.technology.RoutingAddDTO;
import com.maxinhai.platform.dto.technology.RoutingEditDTO;
import com.maxinhai.platform.dto.technology.RoutingQueryDTO;
import com.maxinhai.platform.service.technology.RoutingService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.technology.OperationVO;
import com.maxinhai.platform.vo.technology.RoutingInfoVO;
import com.maxinhai.platform.vo.technology.RoutingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RefreshScope
@RestController
@RequestMapping("/routing")
@Api(tags = "工艺路线管理接口")
public class RoutingController {

    @Resource
    private RoutingService routingService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询工艺路线信息", notes = "根据查询条件分页查询工艺路线信息")
    public AjaxResult<PageResult<RoutingVO>> searchByPage(@RequestBody RoutingQueryDTO param) {
        return AjaxResult.success(PageResult.convert(routingService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取工艺路线信息", notes = "根据工艺路线ID获取详细信息")
    public AjaxResult<RoutingVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(routingService.getInfo(id));
    }

    @PostMapping("/addRouting")
    @ApiOperation(value = "添加工艺路线信息", notes = "添加工艺路线信息")
    public AjaxResult<Void> addRouting(@RequestBody RoutingAddDTO param) {
        routingService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editRouting")
    @ApiOperation(value = "编辑工艺路线信息", notes = "根据工艺路线ID编辑工艺路线信息")
    public AjaxResult<Void> editRouting(@RequestBody RoutingEditDTO param) {
        routingService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeRouting")
    @ApiOperation(value = "删除工艺路线信息", notes = "根据工艺路线ID数组删除工艺路线信息")
    public AjaxResult<Void> removeRouting(@RequestBody String[] ids) {
        routingService.remove(ids);
        return AjaxResult.success();
    }

    @PostMapping("/importExcel")
    @ApiOperation(value = "导入工艺路线数据", notes = "根据Excel模板导入工艺路线数据")
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
        routingService.importExcel(file);
        return AjaxResult.success("Excel数据导入成功！");
    }

    @PostMapping("/queryRoutingDetail/{productCode}/{routingVersion}")
    @ApiOperation(value = "根据产品编码和版本号查询工艺路线明细", notes = "根据产品编码和版本号查询工艺路线明细")
    public AjaxResult<List<OperationVO>> queryRoutingDetail(@PathVariable("productCode") String productCode,
                                                            @PathVariable("routingVersion") String routingVersion) {
        return AjaxResult.success("查询成功！", routingService.queryRoutingDetail(productCode, routingVersion));
    }

    @GetMapping(value = "/queryRoutingInfo/{productCode}/{routingVersion}")
    @ApiOperation(value = "根据产品编码和版本号查询工艺路线信息", notes = "根据产品编码和版本号查询工艺路线信息")
    public AjaxResult<RoutingInfoVO> queryRoutingInfo(@PathVariable("productCode") String productCode,
                                                      @PathVariable("routingVersion") String routingVersion) {
        return AjaxResult.success("查询成功！", routingService.queryRoutingInfo(productCode, routingVersion));
    }

}
