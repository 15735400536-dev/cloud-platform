package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.po.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "用户VO")
public class UserVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "在线状态：true.在线 false.离线")
    private boolean online = false;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

    public static UserVO build(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setAccount(user.getAccount());
        userVO.setUsername(user.getUsername());
        userVO.setSex(user.getSex());
        userVO.setPhone(user.getPhone());
        userVO.setEmail(user.getEmail());
        userVO.setCreateTime(user.getCreateTime());
        userVO.setUpdateTime(user.getUpdateTime());
        return userVO;
    }

}
