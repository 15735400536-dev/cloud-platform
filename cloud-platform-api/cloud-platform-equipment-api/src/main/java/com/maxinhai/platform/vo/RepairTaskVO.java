package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName：RepairTaskVO
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 13:36
 * @Description: 维修任务VO
 */
@Data
@ApiModel(description = "维修任务VO")
public class RepairTaskVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 维修任务编码
     */
    @ApiModelProperty(value = "维修任务编码")
    private String taskCode;
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String equipId;
    /**
     * 故障编码
     */
    @ApiModelProperty(value = "故障编码")
    private String faultCode;
    /**
     * 故障发生时间
     */
    @ApiModelProperty(value = "故障发生时间")
    private Date faultTime;
    /**
     * 保修时间
     */
    @ApiModelProperty(value = "保修时间")
    private Date reportTime;
    /**
     * 报修人
     */
    @ApiModelProperty(value = "报修人")
    private String reporter;
    /**
     * 报修人联系方式
     */
    @ApiModelProperty(value = "报修人联系方式")
    private String reporterContact;

}
