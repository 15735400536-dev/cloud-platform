package com.maxinhai.platform.po.inventory;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存调整单明细表
 */
@Data
@TableName("wms_inventory_adjustment_detail")
public class InventoryAdjustmentDetail extends RecordEntity {

    /**
     * 调整单ID
     */
    private String adjustmentId;
    /**
     * 物料ID
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
     * 调整前数量
     */
    private BigDecimal beforeQty;
    /**
     * 调整前数量
     */
    private BigDecimal adjustmentQty;
    /**
     * 调整后数量
     */
    private BigDecimal afterQty;
    /**
     *  调整原因
     */
    private String reason;

}
