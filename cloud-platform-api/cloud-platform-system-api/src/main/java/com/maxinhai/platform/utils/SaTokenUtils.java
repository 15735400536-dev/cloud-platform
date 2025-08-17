package com.maxinhai.platform.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.po.User;
import org.springframework.stereotype.Component;

@Component
public class SaTokenUtils {

    private static UserMapper userMapper;

    private SaTokenUtils() {
    }

    public SaTokenUtils(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static User getLoginUser() {
        String userId = StpUtil.getLoginIdAsString();
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getAccount, User::getUsername, User::getPhone, User::getEmail)
                .eq(User::getId, userId));
    }

    /**
     * 获取当前登录用户账户
     * @return
     */
    public static String getAccount() {
        User loginUser = getLoginUser();
        return loginUser.getAccount();
    }

    /**
     * 获取当前登录用户昵称
     * @return
     */
    public static String getUsername() {
        User loginUser = getLoginUser();
        return loginUser.getUsername();
    }

    /**
     * 获取当前登录用户邮箱
     * @return
     */
    public static String getEmail() {
        User loginUser = getLoginUser();
        return loginUser.getEmail();
    }

    /**
     * 获取当前登录用户手机号
     * @return
     */
    public static String getPhone() {
        User loginUser = getLoginUser();
        return loginUser.getPhone();
    }

}
