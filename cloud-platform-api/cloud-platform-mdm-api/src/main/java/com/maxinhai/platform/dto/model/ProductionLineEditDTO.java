package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "产线编辑DTO")
public class ProductionLineEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "产线编码")
    private String code;
    @ApiModelProperty(value = "产线名称")
    private String name;

}
