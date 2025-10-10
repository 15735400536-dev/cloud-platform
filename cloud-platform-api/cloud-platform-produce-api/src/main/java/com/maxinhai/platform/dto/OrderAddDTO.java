package com.maxinhai.platform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "订单新增DTO")
public class OrderAddDTO {

    /**
     * 订单编码
     */
    @ApiModelProperty(value = "订单编码")
    private String orderCode;
    /**
     * 订单数量
     */
    @ApiModelProperty(value = "订单数量")
    private Integer qty;
    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型")
    private Integer orderType;
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
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productId;
    /**
     * BOM ID
     */
    @ApiModelProperty(value = "物料清单ID")
    private String bomId;
    /**
     * 工艺路线ID
     */
    @ApiModelProperty(value = "工艺路线ID")
    private String routingId;

}
