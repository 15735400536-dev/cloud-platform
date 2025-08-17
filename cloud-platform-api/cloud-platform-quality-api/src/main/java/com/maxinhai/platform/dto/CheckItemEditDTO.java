package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class CheckItemEditDTO {

    @ApiModelProperty("主键ID")
    private String id;

}
