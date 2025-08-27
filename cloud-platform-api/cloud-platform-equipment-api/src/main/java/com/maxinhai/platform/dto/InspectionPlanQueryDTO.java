package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.InspectionPlanVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "分页查询DTO")
public class InspectionPlanQueryDTO extends PageSearch<InspectionPlanVO> {

    /**
     * 计划编码
     */
    private String planCode;

}
