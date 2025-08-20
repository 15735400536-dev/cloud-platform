package com.maxinhai.platform.po.inventory;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName：InventoryFlow
 * @Author: XinHai.Ma
 * @Date: 2025/8/20 20:24
 * @Description: 库存流水
 */
@Data
@TableName("wms_inventory_flow")
public class InventoryFlow extends RecordEntity {

    /**
     * 关联业务单号（如采购单号、销售单号，可空：如直接调整库存时）
     */
    private String orderCode;
    /**
     * 关联订单类型：1-采购入库单 2-销售出库单 3-调拨单 4-盘点单 5-报废单等
     */
    private Integer orderType;
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
     * 变动前库存数量
     */
    private BigDecimal beforeQty;
    /**
     * 变动后库存数量
     */
    private BigDecimal afterQt;
    /**
     * 变动数量
     */
    private BigDecimal changeQty;
    /**
     * 操作类型：1-入库 2-出库 3-库存调整 4-调拨入库 5-调拨出库
     */
    private OperateType operateType;
    /**
     * 操作人 ID（关联用户表 user.id）
     */
    private String operatorId;
    /**
     * 操作时间（库存实际变动的时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime;

}
