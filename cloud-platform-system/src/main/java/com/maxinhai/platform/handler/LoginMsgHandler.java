package com.maxinhai.platform.handler;

import com.maxinhai.platform.handler.message.IMsgHandler;
import com.maxinhai.platform.handler.message.event.MsgEvent;
import com.maxinhai.platform.handler.message.impl.MsgHandler;
import com.maxinhai.platform.mapper.LoginLogMapper;
import com.maxinhai.platform.po.LoginLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName：SystemMsgHandler
 * @Author: XinHai.Ma
 * @Date: 2025/8/27 17:28
 * @Description: 用户登录消息处理器
 */
@Slf4j
@Component
public class LoginMsgHandler implements IMsgHandler, CommandLineRunner {

    @Resource
    private MsgHandler msgHandler;
    private static final String key = "user_login";
    @Resource
    private LoginLogMapper loginLogMapper;

    @Override
    public void subscribe() {
        msgHandler.subscribe(key, this);
    }

    @Override
    public void handle(MsgEvent event) {
        LoginLog loginLog = (LoginLog) event.getData();
        loginLogMapper.insert(loginLog);
    }

    @Override
    public void run(String... args) throws Exception {
        subscribe();
    }
}
