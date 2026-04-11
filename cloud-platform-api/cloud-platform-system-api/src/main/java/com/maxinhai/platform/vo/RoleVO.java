package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "角色VO")
public class RoleVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "角色标识")
    private String roleKey;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色描述")
    private String roleDesc;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
