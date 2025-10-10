package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "角色编辑DTO")
public class RoleEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "角色标识")
    private String roleKey;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

}
