package com.maxinhai.platform.dto;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.po.LoginLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@ApiModel(description = "登录日志分页查询DTO")
public class LoginLogQueryDTO extends PageSearch<LoginLog> {

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
     * 登录开始时间
     */
    @ApiModelProperty(value = "登录开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginBeginTime;
    /**
     * 登录结束时间
     */
    @ApiModelProperty(value = "登录结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginEndTime;

    public Date getLoginBeginTime() {
        return Objects.isNull(this.loginBeginTime) ? DateUtil.offsetDay(new Date(), -90) : this.loginBeginTime;
    }

    public Date getLoginEndTime() {
        return Objects.isNull(this.loginEndTime) ? new Date() : this.loginEndTime;
    }

}
