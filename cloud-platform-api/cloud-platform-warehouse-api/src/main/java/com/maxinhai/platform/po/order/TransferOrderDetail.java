package com.maxinhai.platform.po.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 移库单明细表
 */
@Data
@TableName("wms_transfer_order_detail")
public class TransferOrderDetail extends RecordEntity {

    /**
     * 移库单ID
     */
    private String transferOrderId;
    /**
     * 物料ID
     */
    private String materialId;
    /**
     * 源货位ID
     */
    private String sourceLocationId;
    /**
     * 目标货位ID
     */
    private String targetLocationId;
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

}
