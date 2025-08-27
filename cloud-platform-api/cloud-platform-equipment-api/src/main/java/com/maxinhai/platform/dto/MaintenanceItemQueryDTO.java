package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaintenanceItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分页查询DTO")
public class MaintenanceItemQueryDTO extends PageSearch<MaintenanceItemVO>{

    /**
     * 保养配置ID
     */
    @ApiModelProperty("保养配置ID")
    private String configId;
    /**
     * 项目编码
     */
    @ApiModelProperty("项目编码")
    private String itemCode;
    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String itemName;

}
