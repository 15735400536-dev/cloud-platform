package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工厂编辑DTO")
public class FactoryEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("工厂编码")
    private String code;
    @ApiModelProperty("工厂名称")
    private String name;

}
