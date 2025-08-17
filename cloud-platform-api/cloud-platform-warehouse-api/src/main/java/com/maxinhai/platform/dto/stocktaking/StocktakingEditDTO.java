package com.maxinhai.platform.dto.stocktaking;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("DTO")
public class StocktakingEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     *  盘点单号
     */
    @ApiModelProperty("盘点单号")
    private String stocktakingNo;
    /**
     *  仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    /**
     * 库区ID（为空则盘点整个仓库）
     */
    @ApiModelProperty("库区ID（为空则盘点整个仓库）")
    private String areaId;
    /**
     * 状态：0-草稿，1-进行中，2-已完成，3-已取消
     */
    @ApiModelProperty("状态：0-草稿，1-进行中，2-已完成，3-已取消")
    private Integer status;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
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
