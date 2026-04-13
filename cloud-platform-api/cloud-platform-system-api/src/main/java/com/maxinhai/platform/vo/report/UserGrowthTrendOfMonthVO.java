package com.maxinhai.platform.vo.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户月增长趋势VO")
public class UserGrowthTrendOfMonthVO {

    @ApiModelProperty(value = "月份")
    private String statMonth;
    @ApiModelProperty(value = "用户数量")
    private Integer userCount;

}
