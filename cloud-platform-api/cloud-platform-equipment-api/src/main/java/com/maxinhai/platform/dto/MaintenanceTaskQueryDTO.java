package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaintenanceTaskVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：MaintenanceTaskQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 13:38
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "DTO")
public class MaintenanceTaskQueryDTO extends PageSearch<MaintenanceTaskVO> {

    /**
     * 保养任务编码
     */
    @ApiModelProperty("保养任务编码")
    private String taskCode;
    /**
     * 保养类型
     */
    @ApiModelProperty("保养类型")
    private String maintenanceType;
    /**
     * 保养等级
     */
    @ApiModelProperty("保养等级")
    private String maintenanceLevel;

}
