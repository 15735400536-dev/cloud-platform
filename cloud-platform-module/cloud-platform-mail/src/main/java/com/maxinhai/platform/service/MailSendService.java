package com.maxinhai.platform.service;

import com.maxinhai.platform.dto.MailSendDTO;

public interface MailSendService {

    /**
     * 发送【纯文本】邮件（支持抄送）
     *
     * @param mailDTO 邮件参数
     */
    void sendSimpleTextMail(MailSendDTO mailDTO);

    /**
     * 发送【富文本HTML】邮件（支持抄送，支持样式/图片/超链接）
     *
     * @param mailDTO 邮件参数
     */
    void sendHtmlMail(MailSendDTO mailDTO);

}
