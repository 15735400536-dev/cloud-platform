package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.CheckItemAddDTO;
import com.maxinhai.platform.dto.CheckItemEditDTO;
import com.maxinhai.platform.dto.CheckItemQueryDTO;
import com.maxinhai.platform.service.CheckItemService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CheckItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/checkItem")
@Api(tags = "检测项管理接口")
public class CheckItemController {

    @Resource
    private CheckItemService checkItemService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询检测项信息", notes = "根据查询条件分页查询检测项信息")
    public AjaxResult<PageResult<CheckItemVO>> searchByPage(@RequestBody CheckItemQueryDTO param) {
        return AjaxResult.success(PageResult.convert(checkItemService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取检测项信息", notes = "根据检测项ID获取详细信息")
    public AjaxResult<CheckItemVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(checkItemService.getInfo(id));
    }

    @PostMapping("/addCheckItem")
    @ApiOperation(value = "添加检测项信息", notes = "添加检测项信息")
    public AjaxResult<Void> addCheckItem(@RequestBody CheckItemAddDTO param) {
        checkItemService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCheckItem")
    @ApiOperation(value = "编辑检测项信息", notes = "根据检测项ID编辑检测项信息")
    public AjaxResult<Void> editCheckItem(@RequestBody CheckItemEditDTO param) {
        checkItemService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCheckItem")
    @ApiOperation(value = "删除检测项信息", notes = "根据检测项ID数组删除检测项信息")
    public AjaxResult<Void> removeCheckItem(@RequestBody String[] ids) {
        checkItemService.remove(ids);
        return AjaxResult.success();
    }

}
