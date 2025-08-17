package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户分页查询DTO")
public class UserQueryDTO extends PageSearch<UserVO> {

    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("用户名")
    private String username;

}
