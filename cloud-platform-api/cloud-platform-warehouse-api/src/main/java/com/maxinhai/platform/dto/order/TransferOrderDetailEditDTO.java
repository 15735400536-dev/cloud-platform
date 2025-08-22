package com.maxinhai.platform.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "DTO")
public class TransferOrderDetailEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 移库单ID
     */
    @ApiModelProperty(value = "移库单ID")
    private String transferOrderId;
    /**
     * 物料ID
     */
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    /**
     * 源货位ID
     */
    @ApiModelProperty(value = "源货位ID")
    private String sourceLocationId;
    /**
     * 目标货位ID
     */
    @ApiModelProperty(value = "目标货位ID")
    private String targetLocationId;
    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNo;
    /**
     * 计划数量
     */
    @ApiModelProperty(value = "计划数量")
    private BigDecimal planQty;
    /**
     * 实际数量
     */
    @ApiModelProperty(value = "实际数量")
    private BigDecimal actualQty;

}
