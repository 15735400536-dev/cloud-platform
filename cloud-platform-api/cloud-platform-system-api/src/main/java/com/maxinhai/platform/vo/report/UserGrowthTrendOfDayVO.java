package com.maxinhai.platform.vo.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户日增长趋势VO")
public class UserGrowthTrendOfDayVO {

    @ApiModelProperty(value = "日期")
    private String statDate;
    @ApiModelProperty(value = "用户数量")
    private Integer userCount;

}
