package com.maxinhai.platform.vo.stocktaking;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("VO")
public class StocktakingVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     *  盘点单号
     */
    private String stocktakingNo;
    /**
     *  仓库ID
     */
    private String warehouseId;
    /**
     * 库区ID（为空则盘点整个仓库）
     */
    private String areaId;
    /**
     * 状态：0-草稿，1-进行中，2-已完成，3-已取消cc
     */
    private Integer status;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间cc
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 操作员c
     */
    private String operator;
    /**
     * 备注
     */
    private String remark;

}
