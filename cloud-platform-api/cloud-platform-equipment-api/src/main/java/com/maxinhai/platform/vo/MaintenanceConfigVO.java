package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "VO")
public class MaintenanceConfigVO {

    @ApiModelProperty(value = "主键ID")
    private String id;

}
