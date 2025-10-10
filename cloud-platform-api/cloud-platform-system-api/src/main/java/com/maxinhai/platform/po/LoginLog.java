package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户登录日志
 */
@Data
@TableName("sys_login_log")
public class LoginLog extends RecordEntity {

    /**
     * 登录账号
     */
    private String account;
    /**
     * 登录用户昵称
     */
    private String username;
    /**
     * 登录IP地址
     */
    private String loginIp;
    /**
     * 登录时间
     */
    private Date loginTime;
    /**
     * 登录平台（PC.网页 Phone.手机应用）
     */
    private String loginPlatform;

}
