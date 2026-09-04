package com.maxinhai.platform.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "移库单明细VO")
public class TransferOrderDetailVO {

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
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    /**
     * 源货位ID
     */
    @ApiModelProperty(value = "源货位ID")
    private String sourceLocationId;
    @ApiModelProperty(value = "源货位编码")
    private String sourceLocationCode;
    @ApiModelProperty(value = "源货位名称")
    private String sourceLocationName;
    /**
     * 目标货位ID
     */
    @ApiModelProperty(value = "目标货位ID")
    private String targetLocationId;
    @ApiModelProperty(value = "目标货位编码")
    private String targetLocationCode;
    @ApiModelProperty(value = "目标货位名称")
    private String targetLocationName;
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
