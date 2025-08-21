package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.CustomConditionAddDTO;
import com.maxinhai.platform.dto.CustomConditionEditDTO;
import com.maxinhai.platform.dto.CustomConditionQueryDTO;
import com.maxinhai.platform.service.CustomConditionService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CustomConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：ConditionController
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:40
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@RestController
@RequestMapping("/condition")
@Api(tags = "查询条件管理接口")
public class ConditionController {

    @Resource
    private CustomConditionService conditionService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询查询条件信息", notes = "根据查询条件分页查询查询条件信息")
    public AjaxResult<Page<CustomConditionVO>> searchByPage(@RequestBody CustomConditionQueryDTO param) {
        return AjaxResult.success(PageResult.convert(conditionService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取查询条件信息", notes = "根据查询条件ID获取详细信息")
    public AjaxResult<CustomConditionVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(conditionService.getInfo(id));
    }

    @PostMapping("/addCustomCondition")
    @ApiOperation(value = "添加查询条件信息", notes = "添加查询条件信息")
    public AjaxResult<Void> addCustomCondition(@RequestBody CustomConditionAddDTO param) {
        conditionService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCustomCondition")
    @ApiOperation(value = "编辑查询条件信息", notes = "根据查询条件ID编辑查询条件信息")
    public AjaxResult<Void> editCustomCondition(@RequestBody CustomConditionEditDTO param) {
        conditionService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCustomCondition")
    @ApiOperation(value = "删除查询条件信息", notes = "根据查询条件ID数组删除查询条件信息")
    public AjaxResult<Void> removeCustomCondition(@RequestBody String[] ids) {
        conditionService.remove(ids);
        return AjaxResult.success();
    }

}
