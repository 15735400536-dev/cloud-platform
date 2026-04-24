package com.maxinhai.platform.vo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

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

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
