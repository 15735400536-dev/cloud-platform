package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "用户绑定角色DTO")
public class UserRoleDTO {

    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "角色ID集合")
    private List<String> roleIds;

}
