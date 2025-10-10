package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "用户登录DTO")
public class LoginDTO {

    @NotBlank(message = "账号不能为空")
    @Size(min = 2, max = 10, message = "账号长度必须在2-10之间")
    private String account;
    @NotBlank(message = "密码不能为空")
    @Size(min = 2, max = 10, message = "密码长度必须在2-10之间")
    private String password;

}
