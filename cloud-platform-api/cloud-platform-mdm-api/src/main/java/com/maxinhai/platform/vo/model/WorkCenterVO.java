package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "加工中心VO")
public class WorkCenterVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 加工中心编码
     */
    @ApiModelProperty(value = "加工中心编码")
    private String code;
    /**
     * 加工中心名称
     */
    @ApiModelProperty(value = "加工中心名称")
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
