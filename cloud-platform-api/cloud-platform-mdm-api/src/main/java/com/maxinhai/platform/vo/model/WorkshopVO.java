package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("车间VO")
public class WorkshopVO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("车间编码")
    private String code;
    @ApiModelProperty("车间名称")
    private String name;
    @ApiModelProperty("工厂ID")
    private String factoryId;
    @ApiModelProperty("工厂编码")
    private String factoryCode;
    @ApiModelProperty("工厂名称")
    private String factoryName;

}
