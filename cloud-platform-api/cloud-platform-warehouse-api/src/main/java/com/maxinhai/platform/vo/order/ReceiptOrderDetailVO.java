package com.maxinhai.platform.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "入库单明细VO")
public class ReceiptOrderDetailVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 入库单ID
     */
    @ApiModelProperty(value = "入库单ID")
    private String receiptOrderId;
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
    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productionDate;
    /**
     * 过期日期
     */
    @ApiModelProperty(value = "过期日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryDate;

}
