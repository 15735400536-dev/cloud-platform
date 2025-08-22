package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class CheckOrderEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;

}
