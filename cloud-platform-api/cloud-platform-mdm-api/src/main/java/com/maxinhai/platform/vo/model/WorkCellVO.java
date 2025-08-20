package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工位VO")
public class WorkCellVO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("工位编码")
    private String code;
    @ApiModelProperty("工位名称")
    private String name;
    /**
     * 加工中心ID
     */
    @ApiModelProperty("加工中心ID")
    private String workCenterId;
    @ApiModelProperty("加工中心编码")
    private String workCenterCode;
    @ApiModelProperty("加工中心名称")
    private String workCenterName;
    /**
     * 产线ID
     */
    @ApiModelProperty("产线ID")
    private String productionLineId;
    @ApiModelProperty("产线编码")
    private String productionLineCode;
    @ApiModelProperty("产线名称")
    private String productionLineName;

}
