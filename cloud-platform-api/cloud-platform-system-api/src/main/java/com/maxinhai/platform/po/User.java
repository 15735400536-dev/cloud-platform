package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
public class User extends RecordEntity {

    private String account;
    private String username;
    private String password;
    private String sex;
    private String phone;
    private String email;

}
