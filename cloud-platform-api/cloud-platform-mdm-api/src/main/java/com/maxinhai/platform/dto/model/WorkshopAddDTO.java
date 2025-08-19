package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("车间新增DTO")
public class WorkshopAddDTO {

    @ApiModelProperty("车间编码")
    private String code;
    @ApiModelProperty("车间名称")
    private String name;

}
