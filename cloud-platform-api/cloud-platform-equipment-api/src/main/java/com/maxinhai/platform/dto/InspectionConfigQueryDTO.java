package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.InspectionConfigVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "分页查询DTO")
public class InspectionConfigQueryDTO extends PageSearch<InspectionConfigVO>{
}
