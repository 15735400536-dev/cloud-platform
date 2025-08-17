package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaintenanceItemVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("分页查询DTO")
public class MaintenanceItemQueryDTO extends PageSearch<MaintenanceItemVO>{
}
