package com.maxinhai.platform.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName：UserLoginDTO
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 15:47
 * @Description: 用户登录DTO
 */
@Data
public class UserLoginDTO {

    @NotBlank(message = "用户名不为空!")
    private String username;

    @NotBlank(message = "密码不为空!")
    private String password;

}
