package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.vo.TaskOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("派工单分页查询DTO")
public class TaskOrderQueryDTO extends PageSearch<TaskOrderVO> {

    /**
     * 派工单编码
     */
    @ApiModelProperty("派工单编码")
    private String taskOrderCode;
    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private OrderStatus status;

}
