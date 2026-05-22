package com.maxinhai.platform.service;

import com.maxinhai.platform.po.User;

import java.util.List;

public interface DistributeTaskOrderToRandomUserService {

    /**
     * 获取随机用户
     * @param count 随即用户数量
     * @return 随即用户集合
     */
    List<User> getRandomUsers(int count);

}
