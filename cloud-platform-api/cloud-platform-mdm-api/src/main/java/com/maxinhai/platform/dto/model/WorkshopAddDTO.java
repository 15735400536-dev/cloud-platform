package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("新增DTO")
public class WorkshopAddDTO {

    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;

}
