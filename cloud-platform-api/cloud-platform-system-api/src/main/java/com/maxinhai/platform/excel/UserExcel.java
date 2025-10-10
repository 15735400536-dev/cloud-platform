package com.maxinhai.platform.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.maxinhai.platform.po.User;
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

    public static User build(UserExcel excel) {
        User user = new User();
        user.setAccount(excel.getAccount());
        user.setUsername(excel.getUsername());
        user.setPassword(excel.getPassword());
        user.setSex(excel.getSex());
        user.setPhone(excel.getPhone());
        user.setEmail(excel.getEmail());
        return user;
    }

}
