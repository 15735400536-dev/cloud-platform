package com.maxinhai.platform.po.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.po.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 移库单表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wms_transfer_order")
public class TransferOrder extends RecordEntity {

    /**
     * 移库单号
     */
    private String transferNo;
    /**
     * 源仓库ID
     */
    private String sourceWarehouseId;
    /**
     * 源仓库ID
     */
    private String targetWarehouseId;
    /**
     * 状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消
     */
    private Integer status;
    /**
     * 移库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date transferTime;
    /**
     * 总数量
     */
    private BigDecimal totalQty;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 备注
     */
    private String remark;

}
