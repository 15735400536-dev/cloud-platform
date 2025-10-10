package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OperateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "派工单操作记录VO")
public class OperateRecordVO {

    /**
     * 派工单ID
     */
    @ApiModelProperty(value = "派工单ID")
    private String taskOrderId;
    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型")
    private OperateType operateType;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime;

}
