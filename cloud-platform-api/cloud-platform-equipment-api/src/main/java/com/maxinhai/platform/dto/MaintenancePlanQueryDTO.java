package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaintenancePlanVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "分页查询DTO")
public class MaintenancePlanQueryDTO extends PageSearch<MaintenancePlanVO> {
}
