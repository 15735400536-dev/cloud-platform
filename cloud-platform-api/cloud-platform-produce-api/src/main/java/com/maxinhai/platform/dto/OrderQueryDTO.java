package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.vo.OrderVO;
import lombok.Data;

@Data
public class OrderQueryDTO extends PageSearch<OrderVO> {

    /**
     * 订单编码
     */
    private String orderCode;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 订单状态
     */
    private OrderStatus orderStatus;

}
