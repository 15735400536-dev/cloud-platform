package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "产线VO")
public class ProductionLineVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "产线编码")
    private String code;
    @ApiModelProperty(value = "产线名称")
    private String name;
    /**
     * 车间ID
     */
    @ApiModelProperty(value = "车间ID")
    private String workshopId;
    @ApiModelProperty(value = "车间编码")
    private String workshopCode;
    @ApiModelProperty(value = "车间名称")
    private String workshopName;

}
