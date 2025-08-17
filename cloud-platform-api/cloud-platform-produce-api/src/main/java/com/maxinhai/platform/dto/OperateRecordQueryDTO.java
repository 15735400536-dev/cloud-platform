package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.vo.OperateRecordVO;
import lombok.Data;

@Data
public class OperateRecordQueryDTO extends PageSearch<OperateRecordVO> {

    /**
     * 派工单ID
     */
    private String taskOrderId;
    /**
     * 操作类型
     */
    private OperateType operateType;

}
