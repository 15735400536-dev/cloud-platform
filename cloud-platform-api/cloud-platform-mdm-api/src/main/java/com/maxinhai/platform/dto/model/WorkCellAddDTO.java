package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工位新增DTO")
public class WorkCellAddDTO {

    @ApiModelProperty(value = "工位编码")
    private String code;
    @ApiModelProperty(value = "工位名称")
    private String name;
    /**
     * 加工中心ID
     */
    @ApiModelProperty(value = "加工中心ID")
    private String workCenterId;
    /**
     * 产线ID
     */
    @ApiModelProperty(value = "产线ID")
    private String productionLineId;

}
