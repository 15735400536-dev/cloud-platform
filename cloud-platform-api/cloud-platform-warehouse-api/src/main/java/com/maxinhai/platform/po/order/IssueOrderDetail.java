package com.maxinhai.platform.po.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 出库单明细表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wms_issue_order_detail")
public class IssueOrderDetail extends RecordEntity {

    /**
     * 出库单ID
     */
    private String issueOrderId;
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

}
