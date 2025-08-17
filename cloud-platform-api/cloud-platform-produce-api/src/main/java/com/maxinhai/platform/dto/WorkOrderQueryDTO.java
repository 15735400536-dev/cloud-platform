package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.vo.WorkOrderVO;
import lombok.Data;

@Data
public class WorkOrderQueryDTO extends PageSearch<WorkOrderVO> {

    /**
     * 工单编码
     */
    private String workOrderCode;
    /**
     * 订单状态
     */
    private OrderStatus orderStatus;

}
