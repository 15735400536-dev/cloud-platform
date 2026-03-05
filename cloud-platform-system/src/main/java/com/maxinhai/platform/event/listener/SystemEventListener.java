package com.maxinhai.platform.event.listener;

import com.maxinhai.platform.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemEventListener {

    @Async // 标记为异步方法
    @EventListener
    public void handleEmailEvent(EmailEvent event) {
        log.info("已向邮箱【{}】发送邮件：【{}】！", event.getEmail(), event.getMessage());
    }

    @Async // 标记为异步方法
    @EventListener
    public void handleQQEvent(QQEvent event) {
        log.info("已向QQ【{}】发送消息：【{}】！", event.getQq(), event.getMessage());
    }

    @Async // 标记为异步方法
    @EventListener
    public void handleWeChatEvent(WeChatEvent event) {
        log.info("已向微信【{}】发送消息：【{}】！", event.getWeChat(), event.getMessage());
    }

    @Async // 标记为异步方法
    @EventListener
    public void handleTextMessageEvent(TextMessageEvent event) {
        log.info("已向手机号【{}】发送短信：【{}】!", event.getPhone(), event.getMessage());
    }

    @Async // 标记为异步方法
    @EventListener
    public void handleUserActionEvent(UserActionEvent event) {
        log.info("监听用户动作->用户：【{}】，全部动作：【{}】", event.getUserId(), event.getAction());
    }

    @Async // 标记为异步方法
    @EventListener(condition = "#event.action.equals('QUERY')")
    public void handleUserQueryActionEvent(UserActionEvent event) {
        log.info("监听用户动作->用户：【{}】，查询动作：【{}】", event.getUserId(), event.getAction());
    }

    @Async // 标记为异步方法
    @EventListener(condition = "#event.action.equals('CREATE')")
    public void handleUserCreateActionEvent(UserActionEvent event) {
        log.info("监听用户动作->用户：【{}】，创建动作：【{}】", event.getUserId(), event.getAction());
    }

    @Async // 标记为异步方法
    @EventListener(condition = "#event.action.equals('UPDATE')")
    public void handleUserUpdateActionEvent(UserActionEvent event) {
        log.info("监听用户动作->用户：【{}】，更新动作：【{}】", event.getUserId(), event.getAction());
    }

    @Async // 标记为异步方法
    @EventListener(condition = "#event.action.equals('DELETE')")
    public void handleUserDeleteActionEvent(UserActionEvent event) {
        log.info("监听用户动作->用户：【{}】，删除动作：【{}】", event.getUserId(), event.getAction());
    }

}
