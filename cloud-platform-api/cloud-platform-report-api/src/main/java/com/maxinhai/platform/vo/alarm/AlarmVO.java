package com.maxinhai.platform.vo.alarm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.AlarmStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "告警记录VO")
public class AlarmVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "算法ID")
    private String algorithmId;
    @ApiModelProperty(value = "算法标识")
    private String algorithmKey;
    @ApiModelProperty(value = "算法名称")
    private String algorithmName;
    @ApiModelProperty(value = "告警消息")
    private String alarmMsg;
    @ApiModelProperty(value = "告警开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "告警结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "告警状态：0.消警 1.告警")
    private AlarmStatus status;

}
