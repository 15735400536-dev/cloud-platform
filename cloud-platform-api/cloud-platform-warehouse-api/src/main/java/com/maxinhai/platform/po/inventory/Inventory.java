package com.maxinhai.platform.po.inventory;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存表
 */
@Data
@TableName("wms_inventory")
public class Inventory extends RecordEntity {

    /**
     * 仓库ID
     */
    private String warehouseId;
    /**
     * 库区ID
     */
    private String areaId;
    /**
     * 货架ID
     */
    private String rackId;
    /**
     * 货位ID
     */
    private String locationId;
    /**
     * 物料ID
     */
    private String materialId;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 批次号
     */
    private BigDecimal qty;
    /**
     * 锁定数量
     */
    private BigDecimal lockedQty;
    /**
     * 可用数量
     */
    private BigDecimal availableQty;
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
