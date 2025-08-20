package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("订单VO")
public class OrderVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 订单编码
     */
    @ApiModelProperty("订单编码")
    private String orderCode;
    /**
     * 订单类型
     */
    @ApiModelProperty("订单类型")
    private Integer orderType;
    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;
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
    @ApiModelProperty("产品编码")
    private String productCode;
    @ApiModelProperty("产品名称")
    private String productName;
    /**
     * BOM ID
     */
    @ApiModelProperty("物料清单ID")
    private String bomId;
    @ApiModelProperty("物料清单编码")
    private String bomCode;
    @ApiModelProperty("物料清单名称")
    private String bomName;
    /**
     * 工艺路线ID
     */
    @ApiModelProperty("工艺路线ID")
    private String routingId;
    @ApiModelProperty("工艺路线编码")
    private String routingCode;
    @ApiModelProperty("工艺路线名称")
    private String routingName;

}
