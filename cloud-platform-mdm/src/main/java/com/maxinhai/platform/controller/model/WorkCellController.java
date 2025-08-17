package com.maxinhai.platform.controller.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.model.WorkCellAddDTO;
import com.maxinhai.platform.dto.model.WorkCellEditDTO;
import com.maxinhai.platform.dto.model.WorkCellQueryDTO;
import com.maxinhai.platform.service.model.WorkCellService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.model.WorkCellVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/workCell")
@Api(tags = "工位管理接口")
public class WorkCellController {

    @Resource
    private WorkCellService workCellService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询工位信息", notes = "根据查询条件分页查询工位信息")
    public AjaxResult<Page<WorkCellVO>> searchByPage(@RequestBody WorkCellQueryDTO param) {
        return AjaxResult.success(PageResult.convert(workCellService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取工位信息", notes = "根据工位ID获取详细信息")
    public AjaxResult<WorkCellVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(workCellService.getInfo(id));
    }

    @PostMapping("/addWorkCell")
    @ApiOperation(value = "添加工位信息", notes = "添加工位信息")
    public AjaxResult<Void> addWorkCell(@RequestBody WorkCellAddDTO param) {
        workCellService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editWorkCell")
    @ApiOperation(value = "编辑工位信息", notes = "根据工位ID编辑工位信息")
    public AjaxResult<Void> editWorkCell(@RequestBody WorkCellEditDTO param) {
        workCellService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeWorkCell")
    @ApiOperation(value = "删除工位信息", notes = "根据工位ID数组删除工位信息")
    public AjaxResult<Void> removeWorkCell(@RequestBody String[] ids) {
        workCellService.remove(ids);
        return AjaxResult.success();
    }

}
