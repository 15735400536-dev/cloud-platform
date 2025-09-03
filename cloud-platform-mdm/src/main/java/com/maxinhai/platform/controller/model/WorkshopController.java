package com.maxinhai.platform.controller.model;

import com.maxinhai.platform.dto.model.WorkshopAddDTO;
import com.maxinhai.platform.dto.model.WorkshopEditDTO;
import com.maxinhai.platform.dto.model.WorkshopQueryDTO;
import com.maxinhai.platform.service.model.WorkshopService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.model.WorkshopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/workshop")
@Api(tags = "车间管理接口")
public class WorkshopController {

    @Resource
    private WorkshopService workshopService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询车间信息", notes = "根据查询条件分页查询车间信息")
    public AjaxResult<PageResult<WorkshopVO>> searchByPage(@RequestBody WorkshopQueryDTO param) {
        return AjaxResult.success(PageResult.convert(workshopService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取车间信息", notes = "根据车间ID获取详细信息")
    public AjaxResult<WorkshopVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(workshopService.getInfo(id));
    }

    @PostMapping("/addWorkshop")
    @ApiOperation(value = "添加车间信息", notes = "添加车间信息")
    public AjaxResult<Void> addWorkshop(@RequestBody WorkshopAddDTO param) {
        workshopService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editWorkshop")
    @ApiOperation(value = "编辑车间信息", notes = "根据车间ID编辑车间信息")
    public AjaxResult<Void> editWorkshop(@RequestBody WorkshopEditDTO param) {
        workshopService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeWorkshop")
    @ApiOperation(value = "删除车间信息", notes = "根据车间ID数组删除车间信息")
    public AjaxResult<Void> removeWorkshop(@RequestBody String[] ids) {
        workshopService.remove(ids);
        return AjaxResult.success();
    }

}
