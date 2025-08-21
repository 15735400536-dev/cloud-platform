package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.CustomViewAddDTO;
import com.maxinhai.platform.dto.CustomViewEditDTO;
import com.maxinhai.platform.dto.CustomViewQueryDTO;
import com.maxinhai.platform.service.CustomViewService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CustomViewVO;
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
@RequestMapping("/view")
@Api(tags = "视图管理接口")
public class ViewController {

    @Resource
    private CustomViewService viewService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询视图信息", notes = "根据视图分页查询视图信息")
    public AjaxResult<Page<CustomViewVO>> searchByPage(@RequestBody CustomViewQueryDTO param) {
        return AjaxResult.success(PageResult.convert(viewService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取视图信息", notes = "根据视图ID获取详细信息")
    public AjaxResult<CustomViewVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(viewService.getInfo(id));
    }

    @PostMapping("/addCustomView")
    @ApiOperation(value = "添加视图信息", notes = "添加视图信息")
    public AjaxResult<Void> addCustomView(@RequestBody CustomViewAddDTO param) {
        viewService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCustomView")
    @ApiOperation(value = "编辑视图信息", notes = "根据视图ID编辑视图信息")
    public AjaxResult<Void> editCustomView(@RequestBody CustomViewEditDTO param) {
        viewService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCustomView")
    @ApiOperation(value = "删除视图信息", notes = "根据视图ID数组删除视图信息")
    public AjaxResult<Void> removeCustomView(@RequestBody String[] ids) {
        viewService.remove(ids);
        return AjaxResult.success();
    }

}
