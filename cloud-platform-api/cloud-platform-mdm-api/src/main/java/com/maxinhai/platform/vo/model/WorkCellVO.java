package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工位VO")
public class WorkCellVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "工位编码")
    private String code;
    @ApiModelProperty(value = "工位名称")
    private String name;
    /**
     * 加工中心ID
     */
    @ApiModelProperty(value = "加工中心ID")
    private String workCenterId;
    @ApiModelProperty(value = "加工中心编码")
    private String workCenterCode;
    @ApiModelProperty(value = "加工中心名称")
    private String workCenterName;
    /**
     * 产线ID
     */
    @ApiModelProperty(value = "产线ID")
    private String productionLineId;
    @ApiModelProperty(value = "产线编码")
    private String productionLineCode;
    @ApiModelProperty(value = "产线名称")
    private String productionLineName;

}
