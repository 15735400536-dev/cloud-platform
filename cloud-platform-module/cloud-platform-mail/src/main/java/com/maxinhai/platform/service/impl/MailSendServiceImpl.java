package com.maxinhai.platform.service.impl;

import com.maxinhai.platform.dto.MailSendDTO;
import com.maxinhai.platform.service.MailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;

/**
 * 邮箱发送核心服务类 - 支持文本/富文本/抄送
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendServiceImpl implements MailSendService {

    /**
     * 注入SpringBoot自动配置的邮件发送核心对象
     */
    private final JavaMailSender javaMailSender;

    /**
     * 从配置文件读取发件人邮箱
     */
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendSimpleTextMail(MailSendDTO mailDTO) {
        try {
            this.sendMail(mailDTO, false);
        } catch (Exception e) {
            log.error("纯文本邮件发送失败：{}", e.getMessage(), e);
            throw new RuntimeException("纯文本邮件发送失败，请检查邮箱配置或收件人地址：" + e.getMessage());
        }
    }

    @Override
    public void sendHtmlMail(MailSendDTO mailDTO) {
        try {
            this.sendMail(mailDTO, true);
        } catch (Exception e) {
            log.error("富文本邮件发送失败：{}", e.getMessage(), e);
            throw new RuntimeException("富文本邮件发送失败，请检查邮箱配置或HTML格式：" + e.getMessage());
        }
    }

    /**
     * 统一邮件发送核心方法
     *
     * @param mailDTO 邮件参数
     * @param isHtml  是否为HTML富文本格式
     */
    private void sendMail(MailSendDTO mailDTO, boolean isHtml) throws Exception {
        // 创建复杂邮件对象，支持附件、抄送、HTML格式
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // MimeMessageHelper：邮件工具类，true表示支持多组件（HTML/附件），UTF-8解决中文乱码
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // 1. 设置发件人
        helper.setFrom(fromEmail);
        // 2. 设置收件人 (多个邮箱转数组)
        helper.setTo(this.splitEmail(mailDTO.getToEmail()));
        // 3. 设置邮件主题
        helper.setSubject(mailDTO.getSubject());

        // 4. 处理抄送：有抄送地址则设置，无则跳过
        if (StringUtils.hasText(mailDTO.getCcEmail())) {
            helper.setCc(this.splitEmail(mailDTO.getCcEmail()));
        }

        // 5. 设置邮件内容：区分纯文本/HTML富文本
        if (isHtml) {
            helper.setText(mailDTO.getHtmlContent(), true);
        } else {
            helper.setText(mailDTO.getTextContent(), false);
        }

        // 6. 发送邮件
        javaMailSender.send(mimeMessage);
        log.info("邮件发送成功！主题：{}，收件人：{}，抄送：{}", mailDTO.getSubject(), mailDTO.getToEmail(), mailDTO.getCcEmail());
    }

    /**
     * 邮箱地址分割：英文逗号分隔的字符串 → 字符串数组
     */
    private String[] splitEmail(String emailStr) {
        if (!StringUtils.hasText(emailStr)) {
            return new String[0];
        }
        return emailStr.split(",");
    }

}
