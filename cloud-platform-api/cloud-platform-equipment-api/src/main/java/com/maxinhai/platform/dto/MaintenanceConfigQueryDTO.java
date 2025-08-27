package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaintenanceConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分页查询DTO")
public class MaintenanceConfigQueryDTO extends PageSearch<MaintenanceConfigVO> {

    /**
     * 配置编码
     */
    @ApiModelProperty(value = "配置编码")
    private String configCode;
    /**
     * 配置名称
     */
    @ApiModelProperty(value = "配置名称")
    private String configName;
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String equipId;

}
