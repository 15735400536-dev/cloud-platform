package com.maxinhai.platform.service;

public interface SimulateUserActionService {

    /**
     * 获取抖音账号直播用户信息
     */
    void getLiveSteamingUser();

    /**
     * 统计用户活动时长
     */
    void countUserActivityDuration();

    /**
     * 根据用户心跳，执行订单创建、派工单开工、暂停、复工、报工动作
     */
    void simulateUserAction();

    /**
     * 更新在线用户状态
     *
     * @param account 账号
     */
    void updateOnlineUser(String account);

    /**
     * 更新离线用户状态
     *
     * @param account 账号
     */
    void updateOfflineUser(String account);

    void updateUserStatus(boolean flag, String account);

}
