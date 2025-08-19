package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("物料类型编辑DTO")
public class MaterialTypeEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("物料类型编码")
    private String code;
    @ApiModelProperty("物料类型名称")
    private String name;
    @ApiModelProperty("物料类型描述")
    private String description;
    @ApiModelProperty("父级ID")
    private String parentId;

}
