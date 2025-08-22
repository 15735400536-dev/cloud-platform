package com.maxinhai.platform.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OperateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName：InventoryFlowVO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 9:54
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "VO")
public class InventoryFlowVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 关联业务单号（如采购单号、销售单号，可空：如直接调整库存时）
     */
    @ApiModelProperty(value = "业务单号（如采购单号、销售单号，可空：如直接调整库存时）")
    private String orderCode;
    /**
     * 关联订单类型：1-采购入库单 2-销售出库单 3-调拨单 4-盘点单 5-报废单等
     */
    @ApiModelProperty(value = "订单类型：1-采购入库单 2-销售出库单 3-调拨单 4-盘点单 5-报废单等")
    private Integer orderType;
    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    private String warehouseCode;
    private String warehouseName;
    /**
     * 库区ID
     */
    @ApiModelProperty(value = "库区ID")
    private String areaId;
    private String areaCode;
    private String areaName;
    /**
     * 货架ID
     */
    @ApiModelProperty(value = "货架ID")
    private String rackId;
    private String rackCode;
    private String rackName;
    /**
     * 货位ID
     */
    @ApiModelProperty(value = "货位ID")
    private String locationId;
    private String locationCode;
    private String locationName;
    /**
     * 物料ID
     */
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    private String materialCode;
    private String materialName;
    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNo;
    /**
     * 变动前库存数量
     */
    @ApiModelProperty(value = "变动前库存数量")
    private BigDecimal beforeQty;
    /**
     * 变动后库存数量
     */
    @ApiModelProperty(value = "变动后库存数量")
    private BigDecimal afterQt;
    /**
     * 变动数量
     */
    @ApiModelProperty(value = "变动数量")
    private BigDecimal changeQty;
    /**
     * 操作类型：1-入库 2-出库 3-库存调整 4-调拨入库 5-调拨出库
     */
    @ApiModelProperty(value = "操作类型：1-入库 2-出库 3-库存调整 4-调拨入库 5-调拨出库")
    private OperateType operateType;
    /**
     * 操作人ID（关联用户表 user.id）
     */
    @ApiModelProperty(value = "操作人ID")
    private String operatorId;
    /**
     * 操作时间（库存实际变动的时间）
     */
    @ApiModelProperty(value = "操作时间（库存实际变动的时间）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime;

}
