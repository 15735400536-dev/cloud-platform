package com.maxinhai.platform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "新增DTO")
public class InspectionPlanAddDTO {

    /**
     * 计划编码
     */
    @ApiModelProperty("计划编码")
    private String planCode;
    /**
     * 巡检配置ID
     */
    @ApiModelProperty("巡检配置ID")
    private String configId;
    /**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
    private String equipId;
    /**
     * 计划日期
     */
    @ApiModelProperty("计划日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planDate;
    /**
     * 计划开始时间
     */
    @ApiModelProperty("计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planBeginTime;
    /**
     * 计划结束时间
     */
    @ApiModelProperty("计划结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planEndTime;
    /**
     * 计划巡检人
     */
    @ApiModelProperty("计划巡检人")
    private String inspector;
    /**
     * 计划状态: 未执行 执行中 已完成 已取消
     */
    @ApiModelProperty("计划状态: 未执行 执行中 已完成 已取消")
    private Integer status;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
