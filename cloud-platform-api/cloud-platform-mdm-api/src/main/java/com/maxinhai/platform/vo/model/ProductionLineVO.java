package com.maxinhai.platform.vo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

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

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
