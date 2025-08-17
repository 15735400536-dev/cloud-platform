package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaintenanceConfigVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("分页查询DTO")
public class MaintenanceConfigQueryDTO extends PageSearch<MaintenanceConfigVO> {
}
