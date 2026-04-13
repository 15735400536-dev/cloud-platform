package com.maxinhai.platform.vo.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户统计VO")
public class UserStatisticsVO {

    @ApiModelProperty(value = "今日新增")
    private int newUserCount;     // 今日新增
    @ApiModelProperty(value = "系统总用户")
    private int totalUserCount;   // 系统总用户
    @ApiModelProperty(value = "在线用户")
    private int onlineUserCount;  // 在线用户
    @ApiModelProperty(value = "男性用户")
    private int menUserCount;  // 男性用户
    @ApiModelProperty(value = "女性用户")
    private int womenUserCount;  // 女性用户
    @ApiModelProperty(value = "未知用户")
    private int unknownUserCount;  // 未知用户
    @ApiModelProperty(value = "当日用户登录次数")
    private int todayLoginUserCount;  // 当日用户登录次数
    @ApiModelProperty(value = "当月用户登录次数")
    private int monthLoginUserCount;  // 当月用户登录次数
    @ApiModelProperty(value = "当年用户登录次数")
    private int yearLoginUserCount;  // 当年用户登录次数

}
