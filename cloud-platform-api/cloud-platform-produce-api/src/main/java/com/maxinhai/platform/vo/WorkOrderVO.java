package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OrderStatus;
import lombok.Data;

import java.util.Date;

@Data
public class WorkOrderVO {

    private String id;
    /**
     * 工单编码
     */
    private String workOrderCode;
    /**
     * 生产数量
     */
    private Integer qty;
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
    private String productCode;
    private String productName;
    /**
     * BOM ID
     */
    private String bomId;
    private String bomCode;
    private String bomName;
    /**
     * 工艺路线ID
     */
    private String routingId;
    private String routingCode;
    private String routingName;

}
