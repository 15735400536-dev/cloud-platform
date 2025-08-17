package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OrderStatus;
import lombok.Data;

import java.util.Date;

@Data
@TableName("prod_task_order")
public class TaskOrder extends RecordEntity {

    /**
     * 派工单编码
     */
    private String taskOrderCode;
    /**
     * 订单状态
     */
    private OrderStatus status;
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
    /**
     * 工序ID
     */
    private String operationId;
    /**
     * 派工单顺修
     */
    private Integer sort;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 工单ID
     */
    private String workOrderId;

}
