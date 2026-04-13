package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "登录日志VO")
public class LoginLogVO {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号")
    private String account;
    /**
     * 登录用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String username;
    /**
     * 登录IP地址
     */
    @ApiModelProperty(value = "IP地址")
    private String loginIp;
    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间")
    private Date loginTime;
    /**
     * 登录平台（PC.网页 Phone.手机应用）
     */
    @ApiModelProperty(value = "登录平台（PC.网页 Phone.手机应用）")
    private String loginPlatform;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
