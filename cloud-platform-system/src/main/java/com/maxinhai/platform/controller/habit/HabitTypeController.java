package com.maxinhai.platform.controller.habit;

import com.maxinhai.platform.dto.habit.HabitTypeAddDTO;
import com.maxinhai.platform.dto.habit.HabitTypeEditDTO;
import com.maxinhai.platform.dto.habit.HabitTypeQueryDTO;
import com.maxinhai.platform.service.habit.HabitTypeService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.habit.HabitTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/habitType")
@Api(tags = "习惯类型管理接口")
@RequiredArgsConstructor
public class HabitTypeController {
    
    private final HabitTypeService habitTypeService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询习惯类型信息", notes = "根据查询条件分页查询习惯类型信息")
    public AjaxResult<PageResult<HabitTypeVO>> searchByPage(@RequestBody HabitTypeQueryDTO param) {
        return AjaxResult.success(PageResult.convert(habitTypeService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取习惯类型信息", notes = "根据习惯类型ID获取详细信息")
    public AjaxResult<HabitTypeVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(habitTypeService.getInfo(id));
    }

    @PostMapping("/addHabitType")
    @ApiOperation(value = "添加习惯类型信息", notes = "添加习惯类型信息")
    public AjaxResult<Void> addHabitType(@RequestBody HabitTypeAddDTO param) {
        habitTypeService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editHabitType")
    @ApiOperation(value = "编辑习惯类型信息", notes = "根据习惯类型ID编辑习惯类型信息")
    public AjaxResult<Void> editHabitType(@RequestBody HabitTypeEditDTO param) {
        habitTypeService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeHabitType")
    @ApiOperation(value = "删除习惯类型信息", notes = "根据习惯类型ID数组删除习惯类型信息")
    public AjaxResult<Void> removeHabitType(@RequestBody String[] ids) {
        habitTypeService.remove(ids);
        return AjaxResult.success();
    }

}
