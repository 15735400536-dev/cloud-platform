package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("编辑DTO")
public class ProductionLineEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;

}
