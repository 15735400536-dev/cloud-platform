package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.LoginLogQueryDTO;
import com.maxinhai.platform.po.LoginLog;
import com.maxinhai.platform.vo.LoginLogVO;

public interface LoginLogService extends IService<LoginLog> {

    Page<LoginLogVO> searchByPage(LoginLogQueryDTO param);

    void crateLoginLog(LoginLog loginLog);

}
