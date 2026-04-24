package com.maxinhai.platform.controller;

import com.maxinhai.platform.mapper.OrderMapper;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.OrderStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report/produce")
@Api(tags = "生产报表管理接口")
@RequiredArgsConstructor
public class ReportController {

    private final OrderMapper orderMapper;

    @PostMapping("/getOrderStatistics")
    @ApiOperation(value = "订单统计", notes = "订单统计")
    public AjaxResult<OrderStatisticsVO> getOrderStatistics() {
        OrderStatisticsVO orderStatistics = orderMapper.getOrderStatistics();
        return AjaxResult.success(orderStatistics);
    }

}
