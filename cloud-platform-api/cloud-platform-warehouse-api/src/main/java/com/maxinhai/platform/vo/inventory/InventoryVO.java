package com.maxinhai.platform.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("VO")
public class InventoryVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库编码")
    private String warehouseCode;
    @ApiModelProperty("仓库名称")
    private String warehouseName;
    /**
     * 库区ID
     */
    @ApiModelProperty("库区ID")
    private String areaId;
    @ApiModelProperty("库区编码")
    private String areaCode;
    @ApiModelProperty("库区名称")
    private String areaName;
    /**
     * 货架ID
     */
    @ApiModelProperty("货架ID")
    private String rackId;
    @ApiModelProperty("货架编码")
    private String rackCode;
    @ApiModelProperty("货架名称")
    private String rackName;
    /**
     * 货位ID
     */
    @ApiModelProperty("货位ID")
    private String locationId;
    @ApiModelProperty("货位编码")
    private String locationCode;
    @ApiModelProperty("货位名称")
    private String locationName;
    /**
     * 物料ID
     */
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    /**
     * 批次号
     */
    @ApiModelProperty("批次号")
    private String batchNo;
    /**
     * 库存数量
     */
    @ApiModelProperty("库存数量")
    private BigDecimal qty;
    /**
     * 锁定数量
     */
    @ApiModelProperty("锁定数量")
    private BigDecimal lockedQty;
    /**
     * 可用数量
     */
    @ApiModelProperty("可用数量")
    private BigDecimal availableQty;
    /**
     * 生产日期
     */
    @ApiModelProperty("生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productionDate;
    /**
     * 过期日期
     */
    @ApiModelProperty("过期日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryDate;

}
