package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "车间VO")
public class WorkshopVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "车间编码")
    private String code;
    @ApiModelProperty(value = "车间名称")
    private String name;
    @ApiModelProperty(value = "工厂ID")
    private String factoryId;
    @ApiModelProperty(value = "工厂编码")
    private String factoryCode;
    @ApiModelProperty(value = "工厂名称")
    private String factoryName;

}
