package com.maxinhai.platform.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "VO")
public class InventoryVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;
    /**
     * 库区ID
     */
    @ApiModelProperty(value = "库区ID")
    private String areaId;
    @ApiModelProperty(value = "库区编码")
    private String areaCode;
    @ApiModelProperty(value = "库区名称")
    private String areaName;
    /**
     * 货架ID
     */
    @ApiModelProperty(value = "货架ID")
    private String rackId;
    @ApiModelProperty(value = "货架编码")
    private String rackCode;
    @ApiModelProperty(value = "货架名称")
    private String rackName;
    /**
     * 货位ID
     */
    @ApiModelProperty(value = "货位ID")
    private String locationId;
    @ApiModelProperty(value = "货位编码")
    private String locationCode;
    @ApiModelProperty(value = "货位名称")
    private String locationName;
    /**
     * 物料ID
     */
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNo;
    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private BigDecimal qty;
    /**
     * 锁定数量
     */
    @ApiModelProperty(value = "锁定数量")
    private BigDecimal lockedQty;
    /**
     * 可用数量
     */
    @ApiModelProperty(value = "可用数量")
    private BigDecimal availableQty;
    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productionDate;
    /**
     * 过期日期
     */
    @ApiModelProperty(value = "过期日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryDate;

}
