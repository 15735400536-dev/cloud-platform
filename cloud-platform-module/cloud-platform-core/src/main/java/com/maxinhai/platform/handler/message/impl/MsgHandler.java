package com.maxinhai.platform.handler.message.impl;

import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.handler.message.IMsgHandler;
import com.maxinhai.platform.handler.message.event.MsgEvent;
import com.maxinhai.platform.utils.FixedSizeEventQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName：MsgHandler
 * @Author: XinHai.Ma
 * @Date: 2025/8/27 17:21
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Component
public class MsgHandler {

    /**
     * 处理器集合
     */
    private Map<String, IMsgHandler> handlerMap = new ConcurrentHashMap<>();
    /**
     * 事件队列
     */
    private FixedSizeEventQueue<MsgEvent> eventQueue = new FixedSizeEventQueue<>(10, FixedSizeEventQueue.OverflowPolicy.OVERWRITE);

    /**
     * 发布事件
     *
     * @param event 消息事件
     */
    public void publish(MsgEvent event) {
        try {
            eventQueue.offer(event);
        } catch (InterruptedException e) {
            throw new BusinessException("发布消息时间出错: " + e);
        }
    }

    /**
     * 消息分发
     */
    @Scheduled(initialDelay = 3000, fixedRate = 1000)
    public void distribute() {
        int size = eventQueue.size();
        if (size == 0) {
            return;
        }
        for (int i = 0; i < size; i++) {
            MsgEvent event = eventQueue.poll();
            IMsgHandler handler = handlerMap.get(event.getKey());
            if (Objects.nonNull(handler)) {
                log.error("无效处理器标识: {}", event.getKey());
                return;
            }

            handler.handle(event);
        }
    }

    /**
     * 订阅
     *
     * @param key     处理器标识
     * @param handler 处理器
     */
    public void subscribe(String key, IMsgHandler handler) {
        handlerMap.put(key, handler);
    }

}
