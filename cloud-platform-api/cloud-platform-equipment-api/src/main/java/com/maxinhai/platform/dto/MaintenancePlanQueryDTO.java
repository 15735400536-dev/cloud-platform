package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaintenancePlanVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分页查询DTO")
public class MaintenancePlanQueryDTO extends PageSearch<MaintenancePlanVO> {

    /**
     * 计划编码
     */
    @ApiModelProperty("计划编码")
    private String planCode;

}
