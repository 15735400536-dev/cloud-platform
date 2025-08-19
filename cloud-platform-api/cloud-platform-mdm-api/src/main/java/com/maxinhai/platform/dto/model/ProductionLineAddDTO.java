package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("新增DTO")
public class ProductionLineAddDTO {

    @ApiModelProperty("产线编码")
    private String code;
    @ApiModelProperty("产线名称")
    private String name;

}
