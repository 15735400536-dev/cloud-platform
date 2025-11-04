package com.maxinhai.platform.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "在线用户VO")
public class OnlineUserBO {

    @ApiModelProperty(value = "账户")
    private String account;
    @ApiModelProperty(value = "用户名")
    private String username;

}
