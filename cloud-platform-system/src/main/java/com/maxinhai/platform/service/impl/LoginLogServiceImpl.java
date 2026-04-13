package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.LoginLogQueryDTO;
import com.maxinhai.platform.mapper.LoginLogMapper;
import com.maxinhai.platform.po.LoginLog;
import com.maxinhai.platform.service.LoginLogService;
import com.maxinhai.platform.vo.LoginLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Override
    public Page<LoginLogVO> searchByPage(LoginLogQueryDTO param) {
        Page<LoginLog> loginLogPage = loginLogMapper.selectPage(param.getPage(), new LambdaQueryWrapper<LoginLog>()
                .like(StrUtil.isNotBlank(param.getAccount()), LoginLog::getAccount, param.getAccount())
                .like(StrUtil.isNotBlank(param.getUsername()), LoginLog::getUsername, param.getUsername())
                .between(Objects.nonNull(param.getLoginBeginTime()) && Objects.nonNull(param.getLoginEndTime()),
                        LoginLog::getLoginTime, param.getLoginBeginTime(), param.getLoginEndTime())
                .orderByDesc(LoginLog::getCreateTime));
        Page<LoginLogVO> pageResult = new Page<>();
        List<LoginLogVO> loginLogVOList = loginLogPage.getRecords().stream().map(record -> {
            LoginLogVO loginLogVO = new LoginLogVO();
            loginLogVO.setId(record.getId());
            loginLogVO.setAccount(record.getAccount());
            loginLogVO.setUsername(record.getUsername());
            loginLogVO.setLoginIp(record.getLoginIp());
            loginLogVO.setLoginTime(record.getLoginTime());
            loginLogVO.setLoginPlatform(record.getLoginPlatform());
            loginLogVO.setCreateTime(record.getCreateTime());
            loginLogVO.setUpdateTime(record.getUpdateTime());
            return loginLogVO;
        }).collect(Collectors.toList());
        pageResult.setRecords(loginLogVOList);
        pageResult.setTotal(loginLogPage.getTotal());
        pageResult.setSize(loginLogPage.getSize());
        pageResult.setCurrent(loginLogPage.getCurrent());
        pageResult.setSearchCount(true);
        pageResult.setOptimizeCountSql(true);
        pageResult.setOrders(loginLogPage.getOrders());
        pageResult.setOptimizeJoinOfCountSql(true);
        pageResult.setMaxLimit(loginLogPage.getMaxLimit());
        pageResult.setCountId(loginLogPage.getCountId());
        return pageResult;
    }

    @Override
    public void crateLoginLog(LoginLog loginLog) {
        loginLogMapper.insert(loginLog);
    }
}
