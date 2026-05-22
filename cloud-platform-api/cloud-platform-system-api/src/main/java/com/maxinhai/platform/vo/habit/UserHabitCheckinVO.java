package com.maxinhai.platform.vo.habit;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserHabitCheckinVO {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "用户账号")
    private String account;
    @ApiModelProperty(value = "用户昵称")
    private String username;

    @ApiModelProperty(value = "习惯类型ID")
    private String habitTypeId;
    @ApiModelProperty(value = "习惯类型编码")
    private String habitTypeCode;
    @ApiModelProperty(value = "习惯类型名称")
    private String habitTypeName;

    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "打开时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkinTime;

}
