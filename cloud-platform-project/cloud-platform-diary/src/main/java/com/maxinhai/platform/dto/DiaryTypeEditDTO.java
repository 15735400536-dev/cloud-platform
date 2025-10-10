package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class DiaryTypeEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("日记类型名称")
    private String name;

}
