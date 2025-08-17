package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class WorkCenterVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 加工中心编码
     */
    @ApiModelProperty("加工中心编码")
    private String code;
    /**
     * 加工中心名称
     */
    @ApiModelProperty("加工中心名称")
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
