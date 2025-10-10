package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.po.LoginLog;

public interface LoginLogService extends IService<LoginLog> {

    void crateLoginLog(LoginLog loginLog);

}
