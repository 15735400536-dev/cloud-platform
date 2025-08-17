package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("角色编辑DTO")
public class RoleEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("角色标识")
    private String roleKey;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("角色描述")
    private String roleDesc;

}
