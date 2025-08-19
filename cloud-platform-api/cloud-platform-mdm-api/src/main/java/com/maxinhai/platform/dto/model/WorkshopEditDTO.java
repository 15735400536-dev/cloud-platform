package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("车间编辑DTO")
public class WorkshopEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("车间编码")
    private String code;
    @ApiModelProperty("车间名称")
    private String name;

}
