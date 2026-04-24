package com.maxinhai.platform.dto.worktime;

import com.maxinhai.platform.dto.PageSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户工时分页查询DTO")
public class UserWorkTimeQueryDTO extends PageSearch<UserWorkTimeQueryDTO> {

    @ApiModelProperty(value = "用户账号")
    private String account;
    @ApiModelProperty(value = "用户昵称")
    private String username;

}
