package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("产线VO")
public class ProductionLineVO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("产线编码")
    private String code;
    @ApiModelProperty("产线名称")
    private String name;
    /**
     * 车间ID
     */
    @ApiModelProperty("车间ID")
    private String workshopId;
    @ApiModelProperty("车间编码")
    private String workshopCode;
    @ApiModelProperty("车间名称")
    private String workshopName;

}
