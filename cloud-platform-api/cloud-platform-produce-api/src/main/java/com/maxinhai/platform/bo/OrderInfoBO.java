package com.maxinhai.platform.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "订单统计信息BO")
public class OrderInfoBO {

    @ApiModelProperty(value = "总数量")
    private long totalQty;
    @ApiModelProperty(value = "完成数量")
    private long finishQty;
    @ApiModelProperty(value = "未完成数量")
    private long unfinishQty;
    @ApiModelProperty(value = "今日完成数量")
    private long todayFinishQty;
    @ApiModelProperty(value = "完成天数")
    private long finishDays;

    public long getUnfinishQty() {
        // 当SQL语句不查询未完成数量，采用总数量-完成数量方式获得未完成数量
        return 0 == unfinishQty ?  totalQty - finishQty : unfinishQty;
    }

}
