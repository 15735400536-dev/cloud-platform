package com.maxinhai.platform.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("VO")
public class TransferOrderDetailVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 移库单ID
     */
    @ApiModelProperty("移库单ID")
    private String transferOrderId;
    /**
     * 物料ID
     */
    @ApiModelProperty("物料ID")
    private String materialId;
    /**
     * 源货位ID
     */
    @ApiModelProperty("源货位ID")
    private String sourceLocationId;
    /**
     * 目标货位ID
     */
    @ApiModelProperty("目标货位ID")
    private String targetLocationId;
    /**
     * 批次号
     */
    @ApiModelProperty("批次号")
    private String batchNo;
    /**
     * 计划数量
     */
    @ApiModelProperty("计划数量")
    private BigDecimal planQty;
    /**
     * 实际数量
     */
    @ApiModelProperty("实际数量")
    private BigDecimal actualQty;

}
