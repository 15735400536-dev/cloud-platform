package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("工单VO")
public class WorkOrderVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 工单编码
     */
    @ApiModelProperty("工单编码")
    private String workOrderCode;
    /**
     * 生产数量
     */
    @ApiModelProperty("生产数量")
    private Integer qty;
    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private OrderStatus orderStatus;
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
     * 实际开始时间
     */
    @ApiModelProperty("实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualBeginTime;
    /**
     * 实际结束时间
     */
    @ApiModelProperty("实际结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualEndTime;
    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private String productId;
    @ApiModelProperty("产品ID")
    private String productCode;
    @ApiModelProperty("产品ID")
    private String productName;
    /**
     * BOM ID
     */
    @ApiModelProperty("物料清单ID")
    private String bomId;
    @ApiModelProperty("物料清单ID")
    private String bomCode;
    @ApiModelProperty("物料清单ID")
    private String bomName;
    /**
     * 工艺路线ID
     */
    @ApiModelProperty("工艺路线ID")
    private String routingId;
    @ApiModelProperty("主键ID")
    private String routingCode;
    @ApiModelProperty("主键ID")
    private String routingName;

}
