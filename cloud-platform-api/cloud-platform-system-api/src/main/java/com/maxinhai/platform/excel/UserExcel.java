package com.maxinhai.platform.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserExcel {

    @ExcelProperty("账号")
    private String account;
    @ExcelProperty("用户名")
    private String username;
    @ExcelProperty("密码")
    private String password;
    @ExcelProperty("性别")
    private String sex;
    @ExcelProperty("手机号")
    private String phone;
    @ExcelProperty("邮箱")
    private String email;

}
