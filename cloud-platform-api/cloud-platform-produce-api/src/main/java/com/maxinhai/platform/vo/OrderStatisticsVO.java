package com.maxinhai.platform.vo;

import com.maxinhai.platform.utils.ProdProgressUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@ApiModel(description = "订单统计VO")
public class OrderStatisticsVO {

    @ApiModelProperty(value = "今日完工订单")
    private int todayFinishOrderCount;
    @ApiModelProperty(value = "订单总数")
    private int totalOrderCount;
    @ApiModelProperty(value = "完工订单总数")
    private int finishOrderCount;
    @ApiModelProperty(value = "在制订单总数")
    private int unFinishOrderCount;
    @ApiModelProperty(value = "初始订单总数")
    private int initOrderCount;
    @ApiModelProperty(value = "订单生产进度")
    private BigDecimal orderProgress;

    @ApiModelProperty(value = "今日完工工单")
    private int todayFinishWorkOrderCount;
    @ApiModelProperty(value = "工单总数")
    private int totalWorkOrderCount;
    @ApiModelProperty(value = "完工工单总数")
    private int finishWorkOrderCount;
    @ApiModelProperty(value = "在制工单总数")
    private int unFinishWorkOrderCount;
    @ApiModelProperty(value = "初始工单总数")
    private int initWorkOrderCount;
    @ApiModelProperty(value = "工单生产进度")
    private BigDecimal workOrderProgress;

    @ApiModelProperty(value = "今日完工派工单")
    private int todayFinishTaskOrderCount;
    @ApiModelProperty(value = "派工单总数")
    private int totalTaskOrderCount;
    @ApiModelProperty(value = "完工派工单总数")
    private int finishTaskOrderCount;
    @ApiModelProperty(value = "在制派工单总数")
    private int unFinishTaskOrderCount;
    @ApiModelProperty(value = "初始派工单总数")
    private int initTaskOrderCount;
    @ApiModelProperty(value = "派工单生产进度")
    private BigDecimal taskOrderProgress;

    public BigDecimal getOrderProgress() {
        if(Objects.isNull(this.orderProgress)) {
            this.orderProgress = ProdProgressUtils.getOrderProgress(this.finishOrderCount, this.totalOrderCount);
        }
        return orderProgress;
    }

    public BigDecimal getWorkOrderProgress() {
        if(Objects.isNull(this.workOrderProgress)) {
            this.workOrderProgress = ProdProgressUtils.getOrderProgress(this.finishWorkOrderCount, this.totalWorkOrderCount);
        }
        return workOrderProgress;
    }

    public BigDecimal getTaskOrderProgress() {
        if(Objects.isNull(this.taskOrderProgress)) {
            this.taskOrderProgress = ProdProgressUtils.getOrderProgress(this.finishTaskOrderCount, this.totalTaskOrderCount);
        }
        return taskOrderProgress;
    }

}
