package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.CustomSqlAddDTO;
import com.maxinhai.platform.dto.CustomSqlEditDTO;
import com.maxinhai.platform.dto.CustomSqlQueryDTO;
import com.maxinhai.platform.service.CustomSqlService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CustomSqlVO;
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
@RequestMapping("/sql")
@Api(tags = "SQL语句管理接口")
public class SqlController {

    @Resource
    private CustomSqlService sqlService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询SQL语句信息", notes = "根据查询条件分页查询SQL语句信息")
    public AjaxResult<Page<CustomSqlVO>> searchByPage(@RequestBody CustomSqlQueryDTO param) {
        return AjaxResult.success(PageResult.convert(sqlService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取SQL语句信息", notes = "根据SQL语句ID获取详细信息")
    public AjaxResult<CustomSqlVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(sqlService.getInfo(id));
    }

    @PostMapping("/addCustomSql")
    @ApiOperation(value = "添加SQL语句信息", notes = "添加SQL语句信息")
    public AjaxResult<Void> addCustomSql(@RequestBody CustomSqlAddDTO param) {
        sqlService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCustomSql")
    @ApiOperation(value = "编辑SQL语句信息", notes = "根据SQL语句ID编辑SQL语句信息")
    public AjaxResult<Void> editCustomSql(@RequestBody CustomSqlEditDTO param) {
        sqlService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCustomSql")
    @ApiOperation(value = "删除SQL语句信息", notes = "根据SQL语句ID数组删除SQL语句信息")
    public AjaxResult<Void> removeCustomSql(@RequestBody String[] ids) {
        sqlService.remove(ids);
        return AjaxResult.success();
    }

}
