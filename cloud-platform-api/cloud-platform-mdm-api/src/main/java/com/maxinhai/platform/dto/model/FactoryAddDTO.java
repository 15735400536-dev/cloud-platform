package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工厂新增DTO")
public class FactoryAddDTO {

    @ApiModelProperty(value = "工厂编码")
    private String code;
    @ApiModelProperty(value = "工厂名称")
    private String name;

}
