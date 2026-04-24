package com.maxinhai.platform.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskOrderWorkTimeBO {

    @ApiModelProperty(value = "派工单ID")
    private String taskOrderId;
    @ApiModelProperty(value = "总工时")
    private long totalWorkHours;

}
