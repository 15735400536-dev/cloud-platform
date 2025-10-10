package com.maxinhai.platform.vo.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "调整单VO")
public class InventoryAdjustmentVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 调整单号
     */
    @ApiModelProperty(value = "调整单号")
    private String adjustmentNo;
    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    /**
     * 调整类型：1-盘盈，2-盘亏，3-其他调整
     */
    @ApiModelProperty(value = "调整类型：1-盘盈，2-盘亏，3-其他调整")
    private Integer adjustmentType;
    /**
     * 状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消
     */
    @ApiModelProperty(value = "状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消")
    private Integer status;
    /**
     * 调整时间
     */
    @ApiModelProperty(value = "调整时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date adjustmentTime;
    /**
     * 调整总数量
     */
    @ApiModelProperty(value = "调整总数量")
    private BigDecimal totalQty;
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
