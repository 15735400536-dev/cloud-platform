package com.maxinhai.platform.dto;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "注册DTO")
public class RegisterDTO {

    @ApiModelProperty(value = "账户", position = 1)
    private String account;
    @ApiModelProperty(value = "用户名", position = 2)
    private String username;
    @ApiModelProperty(value = "密码", position = 3)
    private String password;
    @ApiModelProperty(value = "性别", allowableValues = "男、女、未知", position = 4)
    private String sex;
    @ApiModelProperty(value = "手机号", position = 5)
    private String phone;
    @ApiModelProperty(value = "邮箱", position = 6)
    private String email;

    public String getPassword() {
        // 默认密码：123456
        return StrUtil.isNotBlank(password) ? password : "123456";
    }

}
