package com.maxinhai.platform.po.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库单表
 */
@Data
@TableName("wms_issue_order")
public class IssueOrder extends RecordEntity {

    /**
     * 出库单号
     */
    private String orderNo;
    /**
     * 仓库ID
     */
    private String warehouseId;
    /**
     * 出库类型：1-销售出库，2-生产领料，3-调拨出库，4-报废出库等
     */
    private Integer orderType;
    /**
     * 源单号（如采购单号）
     */
    private String sourceOrderNo;
    /**
     * 状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消
     */
    private Integer status;
    /**
     * 出库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueTime;
    /**
     * 总数量
     */
    private BigDecimal totalQty;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 备注
     */
    private String remark;

}
