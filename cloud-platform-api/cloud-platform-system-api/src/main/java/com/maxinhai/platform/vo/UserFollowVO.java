package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "关注用户VO")
public class UserFollowVO {

    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "关注用户ID")
    private String followId;
    @ApiModelProperty(value = "关注用户账号")
    private String account;
    @ApiModelProperty(value = "关注用户昵称")
    private String username;

}
