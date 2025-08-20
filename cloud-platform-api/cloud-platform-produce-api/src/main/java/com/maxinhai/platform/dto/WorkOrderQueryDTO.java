package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.vo.WorkOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工单分页查询DTO")
public class WorkOrderQueryDTO extends PageSearch<WorkOrderVO> {

    /**
     * 工单编码
     */
    @ApiModelProperty("工单编码")
    private String workOrderCode;
    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private OrderStatus orderStatus;

}
