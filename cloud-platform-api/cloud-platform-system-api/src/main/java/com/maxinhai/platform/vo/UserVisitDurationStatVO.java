package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel(description = "每日各网站浏览时长统计VO")
public class UserVisitDurationStatVO {

    @ApiModelProperty(value = "客户端唯一标识")
    private String clientId;
    @ApiModelProperty(value = "域名")
    private String host;
    @ApiModelProperty(value = "日期")
    private LocalDate visitDay;
    @ApiModelProperty(value = "总时长（毫秒）")
    private Long totalDurationMs;
    @ApiModelProperty(value = "总时长（秒）")
    private Long totalDurationSec;

}
