package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.vo.TaskOrderVO;
import lombok.Data;

@Data
public class TaskOrderQueryDTO extends PageSearch<TaskOrderVO> {

    /**
     * 派工单编码
     */
    private String taskOrderCode;
    /**
     * 订单状态
     */
    private OrderStatus status;

}
