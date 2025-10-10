package com.maxinhai.platform.dto;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户新增DTO")
public class UserAddDTO {

    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "邮箱")
    private String email;

    public String getPassword() {
        // 如果密码为空，默认密码设置为123456
        if (StrUtil.isEmpty(password)) {
            return "123456";
        }
        return password;
    }

}
