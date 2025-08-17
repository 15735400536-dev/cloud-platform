package com.maxinhai.platform.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("DTO")
public class TransferOrderAddDTO {

    /**
     * 移库单号
     */
    @ApiModelProperty("移库单号")
    private String transferNo;
    /**
     * 源仓库ID
     */
    @ApiModelProperty("源仓库ID")
    private String sourceWarehouseId;
    /**
     * 源仓库ID
     */
    @ApiModelProperty("源仓库ID")
    private String targetWarehouseId;
    /**
     * 状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消
     */
    @ApiModelProperty("状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消")
    private Integer status;
    /**
     * 移库时间
     */
    @ApiModelProperty("移库时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date transferTime;
    /**
     * 总数量
     */
    @ApiModelProperty("总数量")
    private BigDecimal totalQty;
    /**
     * 操作员
     */
    @ApiModelProperty("操作员")
    private String operator;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
