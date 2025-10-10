package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "检测单编辑DTO")
public class CheckOrderEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;

}
