package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("编辑DTO")
public class InspectionPlanEditDTO {

    @ApiModelProperty("主键ID")
    private String id;

}
