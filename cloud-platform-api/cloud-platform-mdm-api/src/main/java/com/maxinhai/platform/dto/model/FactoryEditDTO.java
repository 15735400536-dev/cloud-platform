package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工厂编辑DTO")
public class FactoryEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "工厂编码")
    private String code;
    @ApiModelProperty(value = "工厂名称")
    private String name;
    @ApiModelProperty(value = "工厂地址")
    private String address;

}
