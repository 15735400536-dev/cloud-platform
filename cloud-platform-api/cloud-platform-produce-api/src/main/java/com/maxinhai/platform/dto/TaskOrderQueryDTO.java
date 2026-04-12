package com.maxinhai.platform.dto;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.vo.TaskOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@ApiModel(description = "派工单分页查询DTO")
public class TaskOrderQueryDTO extends PageSearch<TaskOrderVO> {

    @ApiModelProperty(value = "订单编码")
    private String orderCode;
    @ApiModelProperty(value = "工单编码")
    private String workOrderCode;

    /**
     * 派工单编码
     */
    @ApiModelProperty(value = "派工单编码")
    private String taskOrderCode;
    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private OrderStatus status;
    @ApiModelProperty(value = "实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualBeginTime;
    @ApiModelProperty(value = "实际结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualEndTime;

    public Date getActualBeginTime() {
        return Objects.isNull(this.actualBeginTime) ? DateUtil.offsetDay(new Date(), -30) : this.actualBeginTime;
    }

    public Date getActualEndTime() {
        return Objects.isNull(this.actualEndTime) ? new Date() : this.actualEndTime;
    }

}
