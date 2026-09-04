package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 网页访问记录接收DTO
 */
@Data
@ApiModel(description = "网页访问记录接收DTO")
public class WebVisitRecordDTO {

    @ApiModelProperty(value = "客户端唯一标识")
    private String clientId;
    @ApiModelProperty(value = "浏览器名称")
    private String browserName;
    @ApiModelProperty(value = "浏览器版本")
    private String browserVersion;
    @ApiModelProperty(value = "访问日期 2026/9/4")
    private String day;
    @ApiModelProperty(value = "访问耗时，毫秒")
    private Integer durationMs;
    @ApiModelProperty(value = "访问公网IP")
    private String publicIp;
    @ApiModelProperty(value = "开始时间戳(毫秒)")
    private Long startTime;
    @ApiModelProperty(value = "时间字符串 18:57:19")
    private String timeStr;
    @ApiModelProperty(value = "网页标题")
    private String title;
    @ApiModelProperty(value = "访问url地址")
    private String url;

}

