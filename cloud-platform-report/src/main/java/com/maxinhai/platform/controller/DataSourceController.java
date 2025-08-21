package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.CustomDataSourceAddDTO;
import com.maxinhai.platform.dto.CustomDataSourceEditDTO;
import com.maxinhai.platform.dto.CustomDataSourceQueryDTO;
import com.maxinhai.platform.service.CustomDataSourceService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CustomDataSourceVO;
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
@RequestMapping("/dataSource")
@Api(tags = "数据源管理接口")
public class DataSourceController {

    @Resource
    private CustomDataSourceService dataSourceService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询数据源信息", notes = "根据查询条件分页查询数据源信息")
    public AjaxResult<Page<CustomDataSourceVO>> searchByPage(@RequestBody CustomDataSourceQueryDTO param) {
        return AjaxResult.success(PageResult.convert(dataSourceService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取数据源信息", notes = "根据数据源ID获取详细信息")
    public AjaxResult<CustomDataSourceVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(dataSourceService.getInfo(id));
    }

    @PostMapping("/addCustomDataSource")
    @ApiOperation(value = "添加数据源信息", notes = "添加数据源信息")
    public AjaxResult<Void> addCustomDataSource(@RequestBody CustomDataSourceAddDTO param) {
        dataSourceService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCustomDataSource")
    @ApiOperation(value = "编辑数据源信息", notes = "根据数据源ID编辑数据源信息")
    public AjaxResult<Void> editCustomDataSource(@RequestBody CustomDataSourceEditDTO param) {
        dataSourceService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCustomDataSource")
    @ApiOperation(value = "删除数据源信息", notes = "根据数据源ID数组删除数据源信息")
    public AjaxResult<Void> removeCustomDataSource(@RequestBody String[] ids) {
        dataSourceService.remove(ids);
        return AjaxResult.success();
    }

}
