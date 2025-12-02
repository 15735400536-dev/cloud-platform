package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "订单进度统计VO")
public class OrderProgressVO {

    @ApiModelProperty(value = "订单总数")
    private long orderTotalQty;
    @ApiModelProperty(value = "订单完工总数")
    private long orderFinishQty;
    @ApiModelProperty(value = "订单未完工总数")
    private long orderUnFinishQty;
    @ApiModelProperty(value = "订单进度（0.00%）")
    private String orderProgress;

    @ApiModelProperty(value = "工单总数")
    private long workOrderTotalQty;
    @ApiModelProperty(value = "工单完工总数")
    private long workOrderFinishQty;
    @ApiModelProperty(value = "工单未完工总数")
    private long workOrderUnFinishQty;
    @ApiModelProperty(value = "工单进度（0.00%）")
    private String workOrderProgress;

    @ApiModelProperty(value = "派工单总数")
    private long taskOrderTotalQty;
    @ApiModelProperty(value = "派工单完工总数")
    private long taskOrderFinishQty;
    @ApiModelProperty(value = "派工单未完工总数")
    private long taskOrderUnFinishQty;
    @ApiModelProperty(value = "派工单进度（0.00%）")
    private String taskOrderProgress;

    @ApiModelProperty(value = "订单今日完工数量")
    private long orderTodayFinishQty;
    @ApiModelProperty(value = "工单今日完工数量")
    private long workOrderTodayFinishQty;
    @ApiModelProperty(value = "派工单今日完工数量")
    private long taskOrderTodayFinishQty;

    @ApiModelProperty(value = "订单平均完工数量")
    private double orderAvgFinishQty;
    @ApiModelProperty(value = "工单平均完工数量")
    private double workOrderAvgFinishQty;
    @ApiModelProperty(value = "派工单平均完工数量")
    private double taskOrderAvgFinishQty;

    @ApiModelProperty(value = "预计完工天数（派工单）")
    private long  expectedFinishDays;
    @ApiModelProperty(value = "实时预计完工天数（派工单）")
    private long  expectedRealTimeFinishDays;
    @ApiModelProperty(value = "实际完工天数（派工单）")
    private long  actualFinishDays;

}
