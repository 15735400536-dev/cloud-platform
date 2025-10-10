package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.CheckLabelQueryDTO;
import com.maxinhai.platform.enums.CheckType;
import com.maxinhai.platform.po.CheckLabel;
import com.maxinhai.platform.service.CheckLabelService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CheckLabelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：ElectronicResumeController
 * @Author: XinHai.Ma
 * @Date: 2025/8/19 16:23
 * @Description: 电子履历管理接口
 */
@RestController
@RequestMapping("/electronicResume")
@Api(tags = "电子履历管理接口")
public class ElectronicResumeController {

    @Resource
    private CheckLabelService checkLabelService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询电子履历标签信息", notes = "根据查询条件分页查询电子履历标签信息")
    public AjaxResult<PageResult<CheckLabelVO>> searchByPage(@RequestBody CheckLabelQueryDTO param) {
        return AjaxResult.success(PageResult.convert(checkLabelService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取电子履历标签信息", notes = "根据电子履历标签ID获取详细信息")
    public AjaxResult<CheckLabelVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(checkLabelService.getInfo(id));
    }

    @GetMapping("/generateLabel/{productId}/{checkType}")
    @ApiOperation(value = "生成电子履历标签", notes = "根据产品ID和检测类型生成电子履历标签")
    public AjaxResult<Void> generateLabel(@PathVariable("productId") String productId,
                                          @PathVariable(name = "checkType", required = false) CheckType checkType) {
        checkLabelService.generateLabel(productId, checkType);
        return AjaxResult.success();
    }

    @GetMapping("/getCheckLabelList/{productId}/{checkType}")
    @ApiOperation(value = "查询电子履历标签", notes = "根据产品ID和检测类型查询电子履历标签")
    public AjaxResult<List<CheckLabel>> getCheckLabelList(@PathVariable("productId") String productId,
                                                          @PathVariable("checkType") CheckType checkType) {
        return AjaxResult.success(checkLabelService.getCheckLabelList(productId, checkType));
    }

    @GetMapping("/getLabelValueMap/{checkOrderId}")
    @ApiOperation(value = "获取电子履历标签以及对应数值", notes = "根据质检单ID获取电子履历标签以及对应数值")
    public AjaxResult<Map<String, String>> getLabelValueMap(@PathVariable("checkOrderId") String checkOrderId) {
        return AjaxResult.success(checkLabelService.getLabelValueMap(checkOrderId));
    }

    @GetMapping("/generate/{workOrderId}")
    @ApiOperation(value = "生成电子履历", notes = "生成电子履历")
    public AjaxResult<byte[]> generate(@PathVariable("workOrderId") String workOrderId) {
        return AjaxResult.success(checkLabelService.generate(workOrderId));
    }

}
