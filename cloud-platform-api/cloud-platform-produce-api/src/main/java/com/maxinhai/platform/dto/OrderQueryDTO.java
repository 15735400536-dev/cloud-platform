package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.vo.OrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单分页查询DTO")
public class OrderQueryDTO extends PageSearch<OrderVO> {

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
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private OrderStatus orderStatus;

}
