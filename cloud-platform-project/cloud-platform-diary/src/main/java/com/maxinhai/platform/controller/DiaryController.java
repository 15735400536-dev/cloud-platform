package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.DiaryAddDTO;
import com.maxinhai.platform.dto.DiaryEditDTO;
import com.maxinhai.platform.dto.DiaryQueryDTO;
import com.maxinhai.platform.service.DiaryService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.DiaryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/diary")
@Api(tags = "日记管理接口")
public class DiaryController {

    @Resource
    private DiaryService diaryService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询日记信息", notes = "根据查询条件分页查询日记信息")
    public AjaxResult<PageResult<DiaryVO>> searchByPage(@RequestBody DiaryQueryDTO param) {
        return AjaxResult.success(PageResult.convert(diaryService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取日记信息", notes = "根据日记ID获取详细信息")
    public AjaxResult<DiaryVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(diaryService.getInfo(id));
    }

    @PostMapping("/addDiary")
    @ApiOperation(value = "添加日记信息", notes = "添加日记信息")
    public AjaxResult<Void> addDiary(@RequestBody DiaryAddDTO param) {
        diaryService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editDiary")
    @ApiOperation(value = "编辑日记信息", notes = "根据日记ID编辑日记信息")
    public AjaxResult<Void> editDiary(@RequestBody DiaryEditDTO param) {
        diaryService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeDiary")
    @ApiOperation(value = "删除日记信息", notes = "根据日记ID数组删除日记信息")
    public AjaxResult<Void> removeDiary(@RequestBody String[] ids) {
        diaryService.remove(ids);
        return AjaxResult.success();
    }
}
