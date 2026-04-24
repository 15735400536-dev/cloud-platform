package com.maxinhai.platform.vo.worktime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户工时VO")
public class UserWorkTimeVO {

    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "用户账号")
    private String account;
    @ApiModelProperty(value = "用户昵称")
    private String username;
    @ApiModelProperty(value = "派工单完工数量")
    private long taskOrderFinishQty;
    @ApiModelProperty(value = "总工时")
    private long totalWorkTime;

}
