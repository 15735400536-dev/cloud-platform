package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.vo.OperateRecordVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("派工单操作记录分页查询DTO")
public class OperateRecordQueryDTO extends PageSearch<OperateRecordVO> {

    /**
     * 派工单ID
     */
    @ApiModelProperty("派工单ID")
    private String taskOrderId;
    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private OperateType operateType;

}
