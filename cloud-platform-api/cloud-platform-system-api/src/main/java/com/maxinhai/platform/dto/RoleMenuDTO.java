package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("角色绑定菜单DTO")
public class RoleMenuDTO {

    @ApiModelProperty("角色ID")
    private String roleId;
    @ApiModelProperty("菜单ID集合")
    private List<String> menuIds;

}
