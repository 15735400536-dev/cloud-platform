package com.maxinhai.platform.dto;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "注册DTO")
public class RegisterDTO {

    @ApiModelProperty(value = "账户")
    private String account;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "密码")
    private String password;

    public String getPassword() {
        return StrUtil.isNotBlank(password) ? password : "123456";
    }

}
