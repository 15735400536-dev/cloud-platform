package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "新增DTO")
public class ProductionLineAddDTO {

    @ApiModelProperty(value = "产线编码")
    private String code;
    @ApiModelProperty(value = "产线名称")
    private String name;
    @ApiModelProperty(value = "车间ID")
    private String workshopId;

}
