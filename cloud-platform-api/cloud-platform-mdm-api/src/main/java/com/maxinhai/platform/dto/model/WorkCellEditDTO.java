package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工位编辑DTO")
public class WorkCellEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "工位编码")
    private String code;
    @ApiModelProperty(value = "工位名称")
    private String name;

}
