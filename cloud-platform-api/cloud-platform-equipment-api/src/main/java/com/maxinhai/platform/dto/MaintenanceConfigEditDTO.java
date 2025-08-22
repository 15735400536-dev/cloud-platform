package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "编辑DTO")
public class MaintenanceConfigEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;

}
