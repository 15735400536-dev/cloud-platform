package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("用户绑定角色DTO")
public class UserRoleDTO {

    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("角色ID集合")
    private List<String> roleIds;

}
