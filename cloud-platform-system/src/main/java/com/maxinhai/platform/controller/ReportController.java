package com.maxinhai.platform.controller;

import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.handler.StringHandler;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.report.UserGrowthTrendVO;
import com.maxinhai.platform.vo.report.UserStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report/user")
@Api(tags = "用户报表管理接口")
@RequiredArgsConstructor
public class ReportController {

    private final UserMapper userMapper;
    private final StringHandler stringHandler;

    @PostMapping("/getUserStatistics")
    @ApiOperation(value = "用户统计", notes = "用户统计")
    public AjaxResult<UserStatisticsVO> getUserStatistics() {
        UserStatisticsVO userStatistics = userMapper.getUserStatistics();
        List<String> tokenKeys = stringHandler.scanKeysWithPrefix("auth:token:");
        userStatistics.setOnlineUserCount(tokenKeys.size());
        return AjaxResult.success(userStatistics);
    }

    @GetMapping("/getUserGrowthTrend")
    @ApiOperation(value = "获取用户增长趋势（按日/月/年切换）", notes = "获取用户增长趋势（按日/月/年切换）")
    public AjaxResult<UserGrowthTrendVO> getUserGrowthTrend(@RequestParam("cycleType") String cycleType) {
        UserGrowthTrendVO userGrowthTrendVO = new UserGrowthTrendVO();
        switch (cycleType) {
            case "day":
                userGrowthTrendVO.setDayData(userMapper.getUserGrowthTrendOfDay());
                break;
            case "month":
                userGrowthTrendVO.setMonthData(userMapper.getUserGrowthTrendOfMonth());
                break;
            case "year":
                userGrowthTrendVO.setYearData(userMapper.getUserGrowthTrendOfYear());
                break;
            default:
                throw new BusinessException("未知周期类型!" + cycleType);
        }
        return AjaxResult.success(userGrowthTrendVO);
    }

}
