package com.maxinhai.platform.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "入库单明细新增DTO")
public class IssueOrderDetailAddDTO {

    /**
     * 出库单ID
     */
    @ApiModelProperty(value = "出库单ID")
    private String issueOrderId;
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
     * 计划数量
     */
    @ApiModelProperty(value = "计划数量")
    private BigDecimal planQty;
    /**
     * 实际数量
     */
    @ApiModelProperty(value = "实际数量")
    private BigDecimal actualQty;
    /**
     * 单位成本
     */
    @ApiModelProperty(value = "单位成本")
    private BigDecimal unitCost;
    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

}
