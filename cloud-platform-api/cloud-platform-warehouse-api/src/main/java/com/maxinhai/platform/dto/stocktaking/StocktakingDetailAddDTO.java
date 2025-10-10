package com.maxinhai.platform.dto.stocktaking;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "盘点单明细新增DTO")
public class StocktakingDetailAddDTO {

    /**
     * 盘点单ID
     */
    @ApiModelProperty(value = "盘点单ID")
    private String stocktakingId;
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
     * 系统库存数量
     */
    @ApiModelProperty(value = "系统库存数量")
    private BigDecimal systemQty;
    /**
     * 实际盘点数量
     */
    @ApiModelProperty(value = "实际盘点数量")
    private BigDecimal actualQty;
    /**
     * 差异数量
     */
    @ApiModelProperty(value = "差异数量")
    private BigDecimal differenceQty;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


}
