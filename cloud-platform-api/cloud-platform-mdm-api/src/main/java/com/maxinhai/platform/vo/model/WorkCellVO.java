package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工位VO")
public class WorkCellVO {

    @ApiModelProperty("主键ID")
    private String id;
    private String code;
    private String name;
    /**
     * 加工中心ID
     */
    private String workCenterId;
    private String workCenterCode;
    private String workCenterName;
    /**
     * 产线ID
     */
    private String productionLineId;
    private String productionLineCode;
    private String productionLineName;

}
