package com.maxinhai.platform.handler.message.impl;

import com.maxinhai.platform.handler.message.IMsgHandler;
import com.maxinhai.platform.handler.message.event.MsgEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName：SystemMsgHandler
 * @Author: XinHai.Ma
 * @Date: 2025/8/27 17:28
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Component
public class SystemMsgHandler implements IMsgHandler {

    @Resource
    private MsgHandler msgHandler;
    private static final String key = "system";

    @Override
    public void subscribe() {
        msgHandler.subscribe(key, this);
    }

    @Override
    public void handle(MsgEvent event) {
        if (key.equals(event.getKey())) {
            log.info("[system] {}", event);
        }
    }
}
