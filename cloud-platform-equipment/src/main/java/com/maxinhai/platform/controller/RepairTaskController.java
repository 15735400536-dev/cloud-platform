package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.RepairTaskQueryDTO;
import com.maxinhai.platform.service.RepairTaskService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.RepairTaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：RepairTaskController
 * @Author: XinHai.Ma
 * @Date: 2025/10/10 15:15
 * @Description: 维修任务管理接口
 */
@RestController
@RequestMapping("/repairTask")
@Api(tags = "维修任务管理接口")
public class RepairTaskController {

    @Resource
    private RepairTaskService repairTaskService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询维修任务信息", notes = "根据查询条件分页查询维修任务信息")
    public AjaxResult<PageResult<RepairTaskVO>> searchByPage(@RequestBody RepairTaskQueryDTO page) {
        return AjaxResult.success(PageResult.convert(repairTaskService.searchByPage(page)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取维修任务信息", notes = "根据维修任务ID获取详细信息")
    public AjaxResult<RepairTaskVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(repairTaskService.getInfo(id));
    }

}
