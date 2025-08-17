package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OrderStatus;
import lombok.Data;

import java.util.Date;

@Data
@TableName("prod_order")
public class Order extends RecordEntity {

    /**
     * 订单编码
     */
    private String orderCode;
    /**
     * 订单数量
     */
    private Integer qty;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 下单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;
    /**
     * 订单状态
     */
    private OrderStatus orderStatus;
    /**
     * 计划开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planBeginTime;
    /**
     * 计划结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planEndTime;
    /**
     * 实际开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualBeginTime;
    /**
     * 实际结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualEndTime;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * BOM ID
     */
    private String bomId;
    /**
     * 工艺路线ID
     */
    private String routingId;

}
