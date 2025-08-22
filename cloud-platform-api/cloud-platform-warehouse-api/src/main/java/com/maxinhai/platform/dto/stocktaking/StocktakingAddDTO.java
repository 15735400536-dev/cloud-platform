package com.maxinhai.platform.dto.stocktaking;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "DTO")
public class StocktakingAddDTO {

    /**
     *  盘点单号
     */
    @ApiModelProperty(value = "盘点单号")
    private String stocktakingNo;
    /**
     *  仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    /**
     * 库区ID（为空则盘点整个仓库）
     */
    @ApiModelProperty(value = "库区ID（为空则盘点整个仓库）")
    private String areaId;
    /**
     * 状态：0-草稿，1-进行中，2-已完成，3-已取消
     */
    @ApiModelProperty(value = "状态：0-草稿，1-进行中，2-已完成，3-已取消")
    private Integer status;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
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
