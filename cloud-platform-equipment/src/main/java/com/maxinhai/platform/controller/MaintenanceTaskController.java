package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.MaintenanceTaskQueryDTO;
import com.maxinhai.platform.service.MaintenanceTaskService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.MaintenanceTaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：MaintenanceTaskController
 * @Author: XinHai.Ma
 * @Date: 2025/10/10 15:14
 * @Description: 保养任务管理接口
 */
@RestController
@RequestMapping("/maintenanceTask")
@Api(tags = "保养任务管理接口")
public class MaintenanceTaskController {

    @Resource
    private MaintenanceTaskService maintenanceTaskService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询保养任务信息", notes = "根据查询条件分页查询保养任务信息")
    public AjaxResult<PageResult<MaintenanceTaskVO>> searchByPage(@RequestBody MaintenanceTaskQueryDTO page) {
        return AjaxResult.success(PageResult.convert(maintenanceTaskService.searchByPage(page)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取保养任务信息", notes = "根据保养任务ID获取详细信息")
    public AjaxResult<MaintenanceTaskVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(maintenanceTaskService.getInfo(id));
    }

}
