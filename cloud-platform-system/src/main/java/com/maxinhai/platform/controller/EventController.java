package com.maxinhai.platform.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.maxinhai.platform.event.*;
import com.maxinhai.platform.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/event")
@Api(tags = "SpringBoot事件管理接口")
@RequiredArgsConstructor
public class EventController {

    // 注入事件发布器（Spring容器自动提供）
    private final ApplicationEventPublisher eventPublisher;

    @ApiOperation(value = "发送邮件事件", notes = "发送邮件事件")
    @GetMapping("sendEmailEvent")
    public AjaxResult<Void> sendEmailEvent() {
        eventPublisher.publishEvent(new EmailEvent(this, "2485460305@qq.com", "欢迎使用邮件通知！"));
        return AjaxResult.success();
    }

    @ApiOperation(value = "发送QQ消息事件", notes = "发送QQ消息事件")
    @GetMapping("sendQQEvent")
    public AjaxResult<Void> sendQQEvent() {
        eventPublisher.publishEvent(new QQEvent(this, "2485460305", "欢迎来到QQ！时间戳：" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        return AjaxResult.success();
    }

    @ApiOperation(value = "发送微信消息事件", notes = "发送微信消息事件")
    @GetMapping("sendWeChatEvent")
    public AjaxResult<Void> sendWeChatEvent() {
        eventPublisher.publishEvent(new WeChatEvent(this, "15735400536", "欢迎来到微信！时间戳：" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        return AjaxResult.success();
    }

    @ApiOperation(value = "发送短信事件", notes = "发送短信事件")
    @GetMapping("sendTextMessageEvent")
    public AjaxResult<Void> sendTextMessageEvent() {
        eventPublisher.publishEvent(new TextMessageEvent(this, "15735400536", "验证码：" + RandomUtil.randomNumbers(6)));
        return AjaxResult.success();
    }

    @ApiOperation(value = "发送用户行为事件", notes = "发送用户行为事件")
    @GetMapping("sendUserActionEvent")
    public AjaxResult<Void> sendUserActionEvent() {
        eventPublisher.publishEvent(new UserActionEvent(this, "XinHai.Ma", "QUERY"));
        eventPublisher.publishEvent(new UserActionEvent(this, "XinHai.Ma", "CREATE"));
        eventPublisher.publishEvent(new UserActionEvent(this, "XinHai.Ma", "UPDATE"));
        eventPublisher.publishEvent(new UserActionEvent(this, "XinHai.Ma", "DELETE"));
        return AjaxResult.success();
    }

}
