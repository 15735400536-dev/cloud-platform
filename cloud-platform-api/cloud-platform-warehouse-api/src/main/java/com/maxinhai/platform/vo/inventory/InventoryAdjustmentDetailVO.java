package com.maxinhai.platform.vo.inventory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("VO")
public class InventoryAdjustmentDetailVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 调整单ID
     */
    @ApiModelProperty("调整单ID")
    private String adjustmentId;
    /**
     * 物料ID
     */
    @ApiModelProperty("物料ID")
    private String materialId;
    /**
     * 货位ID
     */
    @ApiModelProperty("货位ID")
    private String locationId;
    /**
     * 批次号
     */
    @ApiModelProperty("批次号")
    private String batchNo;
    /**
     * 调整前数量
     */
    @ApiModelProperty("调整前数量")
    private BigDecimal beforeQty;
    /**
     * 调整前数量
     */
    @ApiModelProperty("调整前数量")
    private BigDecimal adjustmentQty;
    /**
     * 调整后数量
     */
    @ApiModelProperty("调整后数量")
    private BigDecimal afterQty;
    /**
     *  调整原因
     */
    @ApiModelProperty("调整原因")
    private String reason;

}
