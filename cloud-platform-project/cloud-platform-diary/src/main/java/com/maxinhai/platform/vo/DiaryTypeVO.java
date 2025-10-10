package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "VO")
public class DiaryTypeVO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("日记类型名称")
    private String name;

}
