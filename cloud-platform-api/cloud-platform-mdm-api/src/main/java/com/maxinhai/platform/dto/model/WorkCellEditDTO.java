package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工位编辑DTO")
public class WorkCellEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("工位编码")
    private String code;
    @ApiModelProperty("工位名称")
    private String name;

}
