package com.maxinhai.platform.dto.alarm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "实时告警DTO")
public class RealTimeAlarmDTO {

    @ApiModelProperty(value = "算法标识")
    private String algorithmKey;
    @ApiModelProperty(value = "告警状态：true.发起告警 false.取消告警")
    private Boolean alarmStatus;
    @ApiModelProperty(value = "告警时间")
    private Date alarmTime;
    @ApiModelProperty(value = "告警图片（Base64编码）")
    private List<String> imgs;

}
