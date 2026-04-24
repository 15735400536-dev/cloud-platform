package com.maxinhai.platform.controller;

import com.maxinhai.platform.annotation.ApiLog;
import com.maxinhai.platform.dto.worktime.UserWorkTimeQueryDTO;
import com.maxinhai.platform.service.WorkTimeService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.worktime.CountTaskFinishQtyVO;
import com.maxinhai.platform.vo.worktime.OrderWorkTimeVO;
import com.maxinhai.platform.vo.worktime.UserWorkTimeVO;
import com.maxinhai.platform.vo.worktime.WorkOrderWorkTimeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/workTime")
@Api(tags = "工时管理接口")
public class WorkTimeController {

    @Resource
    private WorkTimeService workTimeService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询用户工时", notes = "根据查询条件分页查询用户工时")
    public AjaxResult<PageResult<UserWorkTimeVO>> searchByPage(@RequestBody UserWorkTimeQueryDTO param) {
        return AjaxResult.success(PageResult.convert(workTimeService.searchPage(param)));
    }

    @ApiLog("根据工单ID获取工单工时")
    @GetMapping("/getWorkOrderWorkTime/{workOrderId}")
    @ApiOperation(value = "获取工单工时", notes = "根据工单ID获取工单工时")
    public AjaxResult<WorkOrderWorkTimeVO> getWorkOrderWorkTime(@PathVariable("workOrderId") String workOrderId) {
        return AjaxResult.success(workTimeService.getWorkOrderWorkTime(workOrderId));
    }

    @ApiLog("根据订单ID获取订单工时")
    @GetMapping("/getOrderWorkTime/{orderId}")
    @ApiOperation(value = "获取订单工时", notes = "根据订单ID获取订单工时")
    public AjaxResult<OrderWorkTimeVO> getOrderWorkTime(@PathVariable("orderId") String orderId) {
        return AjaxResult.success(workTimeService.getOrderWorkTime(orderId));
    }

    @ApiLog("根据工单维度统计派工单完成数")
    @GetMapping("/countTaskFinishQtyByWorkOrderId")
    @ApiOperation(value = "根据工单维度统计派工单完成数", notes = "根据工单维度统计派工单完成数")
    public AjaxResult<List<CountTaskFinishQtyVO>> countTaskFinishQtyByWorkOrderId() {
        return AjaxResult.success(workTimeService.countTaskFinishQtyByWorkOrderId());
    }

    @ApiLog("根据订单维度统计派工单完成数")
    @GetMapping("/countTaskFinishQtyByOrderId")
    @ApiOperation(value = "根据订单维度统计派工单完成数", notes = "根据订单维度统计派工单完成数")
    public AjaxResult<List<CountTaskFinishQtyVO>> countTaskFinishQtyByOrderId() {
        return AjaxResult.success(workTimeService.countTaskFinishQtyByOrderId());
    }

}
