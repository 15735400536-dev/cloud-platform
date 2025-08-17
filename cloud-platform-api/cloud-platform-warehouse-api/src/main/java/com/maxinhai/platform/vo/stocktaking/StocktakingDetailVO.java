package com.maxinhai.platform.vo.stocktaking;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("VO")
public class StocktakingDetailVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 盘点单ID
     */
    @ApiModelProperty("盘点单ID")
    private String stocktakingId;
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
     * 系统库存数量
     */
    @ApiModelProperty("系统库存数量")
    private BigDecimal systemQty;
    /**
     * 实际盘点数量
     */
    @ApiModelProperty("实际盘点数量")
    private BigDecimal actualQty;
    /**
     * 差异数量
     */
    @ApiModelProperty("差异数量")
    private BigDecimal differenceQty;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
