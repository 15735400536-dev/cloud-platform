package com.maxinhai.platform.po.stocktaking;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 盘点单明细表
 */
@Data
@TableName("wms_stocktaking_detail")
public class StocktakingDetail extends RecordEntity {

    /**
     * 盘点单ID
     */
    private String stocktakingId;
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
     * 系统库存数量
     */
    private BigDecimal systemQty;
    /**
     * 实际盘点数量
     */
    private BigDecimal actualQty;
    /**
     * 差异数量
     */
    private BigDecimal differenceQty;
    /**
     * 备注
     */
    private String remark;

}
