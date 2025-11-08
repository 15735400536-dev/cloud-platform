package com.maxinhai.platform.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户BO")
public class UserBO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "邮箱")
    private String email;

}
