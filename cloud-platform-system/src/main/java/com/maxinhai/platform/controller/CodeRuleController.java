package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.CodeRuleAddDTO;
import com.maxinhai.platform.dto.CodeRuleEditDTO;
import com.maxinhai.platform.dto.CodeRuleQueryDTO;
import com.maxinhai.platform.service.CodeRuleService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CodeRuleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/codeRule")
@Api(tags = "编码规则管理接口")
public class CodeRuleController {

    @Resource
    private CodeRuleService codeRuleService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询编码规则信息", notes = "根据查询条件分页查询编码规则信息")
    public AjaxResult<Page<CodeRuleVO>> searchByPage(@RequestBody CodeRuleQueryDTO param) {
        return AjaxResult.success(PageResult.convert(codeRuleService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取编码规则信息", notes = "根据编码规则ID获取详细信息")
    public AjaxResult<CodeRuleVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(codeRuleService.getInfo(id));
    }

    @PostMapping("/addCodeRule")
    @ApiOperation(value = "添加编码规则信息", notes = "添加编码规则信息")
    public AjaxResult<Void> addCodeRule(@RequestBody CodeRuleAddDTO param) {
        codeRuleService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCodeRule")
    @ApiOperation(value = "编辑编码规则信息", notes = "根据编码规则ID编辑编码规则信息")
    public AjaxResult<Void> editCodeRule(@RequestBody CodeRuleEditDTO param) {
        codeRuleService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCodeRule")
    @ApiOperation(value = "删除编码规则信息", notes = "根据编码规则ID数组删除编码规则信息")
    public AjaxResult<Void> removeCodeRule(@RequestBody String[] ids) {
        codeRuleService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/generateCode/{codeRule}/{batchSize}")
    @ApiOperation(value = "批量生成编码", notes = "根据编码规则批量生成编码")
    public AjaxResult<List<String>> generateCode(@PathVariable("codeRule") String codeRule,
                                                 @PathVariable("batchSize") Integer batchSize) {
        return AjaxResult.success(codeRuleService.generateCode(codeRule, batchSize));
    }
}
