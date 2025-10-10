package com.maxinhai.platform.vo.inventory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "调整单明细VO")
public class InventoryAdjustmentDetailVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 调整单ID
     */
    @ApiModelProperty(value = "调整单ID")
    private String adjustmentId;
    /**
     * 物料ID
     */
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    /**
     * 货位ID
     */
    @ApiModelProperty(value = "货位ID")
    private String locationId;
    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNo;
    /**
     * 调整前数量
     */
    @ApiModelProperty(value = "调整前数量")
    private BigDecimal beforeQty;
    /**
     * 调整前数量
     */
    @ApiModelProperty(value = "调整前数量")
    private BigDecimal adjustmentQty;
    /**
     * 调整后数量
     */
    @ApiModelProperty(value = "调整后数量")
    private BigDecimal afterQty;
    /**
     *  调整原因
     */
    @ApiModelProperty(value = "调整原因")
    private String reason;

}
