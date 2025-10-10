package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "派工单VO")
public class TaskOrderVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 派工单编码
     */
    @ApiModelProperty(value = "派工单编码")
    private String taskOrderCode;
    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private OrderStatus orderStatus;
    /**
     * 计划开始时间
     */
    @ApiModelProperty(value = "计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planBeginTime;
    /**
     * 计划结束时间
     */
    @ApiModelProperty(value = "计划结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planEndTime;
    /**
     * 实际开始时间
     */
    @ApiModelProperty(value = "实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualBeginTime;
    /**
     * 实际结束时间
     */
    @ApiModelProperty(value = "实际结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualEndTime;
    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productId;
    @ApiModelProperty(value = "产品编码")
    private String productCode;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    /**
     * BOM ID
     */
    @ApiModelProperty(value = "物料清单ID")
    private String bomId;
    @ApiModelProperty(value = "物料清单编码")
    private String bomCode;
    @ApiModelProperty(value = "物料清单名称")
    private String bomName;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(value = "工艺路线ID")
    private String routingId;
    @ApiModelProperty(value = "工艺路线编码")
    private String routingCode;
    @ApiModelProperty(value = "工艺路线名称")
    private String routingName;
    /**
     * 工序ID
     */
    @ApiModelProperty(value = "工序ID")
    private String operationId;
    @ApiModelProperty(value = "工序编码")
    private String operationCode;
    @ApiModelProperty(value = "工序名称")
    private String operationName;
    /**
     * 派工单顺修
     */
    @ApiModelProperty(value = "派工单顺修")
    private Integer sort;

}
