package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "物料类型新增DTO")
public class MaterialTypeAddDTO {

    @ApiModelProperty(value = "物料类型编码")
    private String code;
    @ApiModelProperty(value = "物料类型名称")
    private String name;
    @ApiModelProperty(value = "物料类型描述")
    private String description;
    @ApiModelProperty(value = "父级ID")
    private String parentId;

}
