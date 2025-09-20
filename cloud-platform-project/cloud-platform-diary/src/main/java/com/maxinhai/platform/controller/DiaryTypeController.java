package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.DiaryTypeAddDTO;
import com.maxinhai.platform.dto.DiaryTypeEditDTO;
import com.maxinhai.platform.dto.DiaryTypeQueryDTO;
import com.maxinhai.platform.service.DiaryTypeService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.DiaryTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/diaryType")
@Api(tags = "日记类型管理接口")
public class DiaryTypeController {

    @Resource
    private DiaryTypeService diaryTypeService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询日记类型信息", notes = "根据查询条件分页查询日记类型信息")
    public AjaxResult<PageResult<DiaryTypeVO>> searchByPage(@RequestBody DiaryTypeQueryDTO param) {
        return AjaxResult.success(PageResult.convert(diaryTypeService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取日记类型信息", notes = "根据日记类型ID获取详细信息")
    public AjaxResult<DiaryTypeVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(diaryTypeService.getInfo(id));
    }

    @PostMapping("/addDiaryType")
    @ApiOperation(value = "添加日记类型信息", notes = "添加日记类型信息")
    public AjaxResult<Void> addDiaryType(@RequestBody DiaryTypeAddDTO param) {
        diaryTypeService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editDiaryType")
    @ApiOperation(value = "编辑日记类型信息", notes = "根据日记类型ID编辑日记类型信息")
    public AjaxResult<Void> editDiaryType(@RequestBody DiaryTypeEditDTO param) {
        diaryTypeService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeDiaryType")
    @ApiOperation(value = "删除日记类型信息", notes = "根据日记类型ID数组删除日记类型信息")
    public AjaxResult<Void> removeDiaryType(@RequestBody String[] ids) {
        diaryTypeService.remove(ids);
        return AjaxResult.success();
    }
}
