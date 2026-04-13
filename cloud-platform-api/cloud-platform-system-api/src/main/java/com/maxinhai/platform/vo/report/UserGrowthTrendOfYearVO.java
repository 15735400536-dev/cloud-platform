package com.maxinhai.platform.vo.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户年增长趋势VO")
public class UserGrowthTrendOfYearVO {

    @ApiModelProperty(value = "年份")
    private String statYear;
    @ApiModelProperty(value = "用户数量")
    private Integer userCount;

}
