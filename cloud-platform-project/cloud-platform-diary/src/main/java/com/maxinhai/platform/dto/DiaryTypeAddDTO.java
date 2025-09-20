package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class DiaryTypeAddDTO {

    @ApiModelProperty("日记类型名称")
    private String name;

}
