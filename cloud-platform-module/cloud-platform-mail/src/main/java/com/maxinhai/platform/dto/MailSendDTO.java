package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 邮件发送请求参数实体类
 */
@Data
@ApiModel(description = "邮件发送DTO")
public class MailSendDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收件人邮箱地址 (多个用英文逗号分隔) 必填
     */
    @NotEmpty(message = "收件人邮箱不能为空")
    @ApiModelProperty(value = "收件人邮箱地址 (多个用英文逗号分隔) 必填")
    private String toEmail;

    /**
     * 抄送邮箱地址 (多个用英文逗号分隔) 可选
     */
    @ApiModelProperty(value = "抄送邮箱地址 (多个用英文逗号分隔) 可选")
    private String ccEmail;

    /**
     * 邮件主题 必填
     */
    @NotBlank(message = "邮件主题不能为空")
    @ApiModelProperty(value = "邮件主题 必填")
    private String subject;

    /**
     * 纯文本邮件内容 (简单文本用这个)
     */
    @ApiModelProperty(value = "纯文本邮件内容 (简单文本用这个)")
    private String textContent;

    /**
     * 富文本邮件内容 (HTML格式，支持样式、图片、超链接等)
     */
    @ApiModelProperty(value = "富文本邮件内容 (HTML格式，支持样式、图片、超链接等)")
    private String htmlContent;

}
