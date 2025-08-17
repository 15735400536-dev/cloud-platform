package com.maxinhai.platform.po.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库单明细表
 */
@Data
@TableName("wms_receipt_order_detail")
public class ReceiptOrderDetail extends RecordEntity {

    /**
     * 入库单ID
     */
    private String receiptOrderId;
    /**
     * 物料IDc
     */
    private String materialId;
    /**
     * 货位ID
     */
    private String locationId;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 计划数量
     */
    private BigDecimal planQty;
    /**
     * 实际数量
     */
    private BigDecimal actualQty;
    /**
     * 单位成本
     */
    private BigDecimal unitCost;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productionDate;
    /**
     * 过期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryDate;


}
