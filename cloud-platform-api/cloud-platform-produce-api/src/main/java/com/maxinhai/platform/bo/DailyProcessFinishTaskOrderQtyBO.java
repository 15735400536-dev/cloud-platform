package com.maxinhai.platform.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "订单统计信息BO")
public class DailyProcessFinishTaskOrderQtyBO {

    @ApiModelProperty(value = "日期")
    private String daily;
    @ApiModelProperty(value = "工序ID")
    private String operationId;
    @ApiModelProperty(value = "工序编码")
    private String operationCode;
    @ApiModelProperty(value = "工序名称")
    private String operationName;
    @ApiModelProperty(value = "派工单完成数量")
    private long qty;

}
