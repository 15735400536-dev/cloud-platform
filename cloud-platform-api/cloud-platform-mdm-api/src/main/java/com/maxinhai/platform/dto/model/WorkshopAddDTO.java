package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "车间新增DTO")
public class WorkshopAddDTO {

    @ApiModelProperty(value = "车间编码")
    private String code;
    @ApiModelProperty(value = "车间名称")
    private String name;

}
