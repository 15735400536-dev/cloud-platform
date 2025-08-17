package com.maxinhai.platform.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("DTO")
public class ReceiptOrderDetailEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 入库单ID
     */
    @ApiModelProperty("入库单ID")
    private String receiptOrderId;
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
     * 计划数量
     */
    @ApiModelProperty("计划数量")
    private BigDecimal planQty;
    /**
     * 实际数量
     */
    @ApiModelProperty("实际数量")
    private BigDecimal actualQty;
    /**
     * 单位成本
     */
    @ApiModelProperty("单位成本")
    private BigDecimal unitCost;
    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private BigDecimal amount;
    /**
     * 生产日期
     */
    @ApiModelProperty("生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productionDate;
    /**
     * 过期日期
     */
    @ApiModelProperty("过期日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryDate;

}
