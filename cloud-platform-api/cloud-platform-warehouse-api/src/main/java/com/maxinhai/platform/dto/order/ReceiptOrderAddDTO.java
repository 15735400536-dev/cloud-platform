package com.maxinhai.platform.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "出库单新增DTO")
public class ReceiptOrderAddDTO {

    /**
     * 入库单号
     */
    @ApiModelProperty(value = "入库单号")
    private String orderNo;
    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    /**
     * 入库类型：1-采购入库，2-生产入库，3-退货入库，4-调拨入库等
     */
    @ApiModelProperty(value = "入库类型：1-采购入库，2-生产入库，3-退货入库，4-调拨入库等")
    private Integer orderType;
    /**
     * 源单号（如销售单号）
     */
    @ApiModelProperty(value = "源单号（如销售单号）")
    private String sourceOrderNo;
    /**
     * 状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消
     */
    @ApiModelProperty(value = "状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消")
    private Integer status;
    /**
     * 入库时间
     */
    @ApiModelProperty(value = "入库时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiptTime;
    /**
     * 总数量
     */
    @ApiModelProperty(value = "总数量")
    private BigDecimal totalQty;
    /**
     * 总金额
     */
    @ApiModelProperty(value = "总金额")
    private BigDecimal totalAmount;
    /**
     * 操作员
     */
    @ApiModelProperty(value = "操作员")
    private String operator;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
