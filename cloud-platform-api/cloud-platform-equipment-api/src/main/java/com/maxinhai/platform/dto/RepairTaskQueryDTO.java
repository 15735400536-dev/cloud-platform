package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.RepairTaskVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：RepairTaskQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 13:37
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "DTO")
public class RepairTaskQueryDTO extends PageSearch<RepairTaskVO> {

    /**
     * 维修任务编码
     */
    @ApiModelProperty("维修任务编码")
    private String taskCode;

}
