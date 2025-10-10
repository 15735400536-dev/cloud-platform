package com.maxinhai.platform.po.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 移库单明细表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
     * 源仓库ID
     */
    private String sourceWarehouseId;
    /**
     * 源库区ID
     */
    private String sourceAreaId;
    /**
     * 源货架ID
     */
    private String sourceRackId;
    /**
     * 源货位ID
     */
    private String sourceLocationId;

    /**
     * 目标仓库ID
     */
    private String targetWarehouseId;
    /**
     * 目标库区ID
     */
    private String targetAreaId;
    /**
     * 目标货架ID
     */
    private String targetRackId;
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
