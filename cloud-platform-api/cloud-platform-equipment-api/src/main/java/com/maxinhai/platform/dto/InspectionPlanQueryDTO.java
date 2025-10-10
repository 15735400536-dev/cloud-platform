package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.InspectionPlanVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "巡检计划分页查询DTO")
public class InspectionPlanQueryDTO extends PageSearch<InspectionPlanVO> {

    /**
     * 计划编码
     */
    private String planCode;

}
