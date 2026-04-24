package com.maxinhai.platform.vo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

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

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
