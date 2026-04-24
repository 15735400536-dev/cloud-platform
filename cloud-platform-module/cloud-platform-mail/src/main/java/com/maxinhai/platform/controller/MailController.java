package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.MailSendDTO;
import com.maxinhai.platform.service.MailSendService;
import com.maxinhai.platform.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件发送测试接口
 */
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
@Api(tags = "邮箱管理接口")
public class MailController {

    private final MailSendService mailSendService;

    /**
     * 发送纯文本邮件接口
     */
    @PostMapping("/send/text")
    @ApiOperation(value = "发送纯文本邮件接口", notes = "发送纯文本邮件接口")
    public AjaxResult<String> sendTextMail(@Validated @RequestBody MailSendDTO mailDTO) {
        mailSendService.sendSimpleTextMail(mailDTO);
        return AjaxResult.success("纯文本邮件发送成功！");
    }

    /**
     * 发送富文本HTML邮件接口
     */
    @PostMapping("/send/html")
    @ApiOperation(value = "发送富文本HTML邮件接口", notes = "发送富文本HTML邮件接口")
    public AjaxResult<String> sendHtmlMail(@Validated @RequestBody MailSendDTO mailDTO) {
        mailSendService.sendHtmlMail(mailDTO);
        return AjaxResult.success("富文本邮件发送成功！");
    }

}
