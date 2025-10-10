package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.RoleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "角色分页查询DTO")
public class RoleQueryDTO extends PageSearch<RoleVO> {

    @ApiModelProperty(value = "角色标识")
    private String roleKey;
    @ApiModelProperty(value = "角色名称")
    private String roleName;

}
