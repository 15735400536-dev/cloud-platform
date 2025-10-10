package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.RepairTaskVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName：RepairTaskQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 13:37
 * @Description: 维修任务分页查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "维修任务分页查询DTO")
public class RepairTaskQueryDTO extends PageSearch<RepairTaskVO> {

    /**
     * 维修任务编码
     */
    @ApiModelProperty("维修任务编码")
    private String taskCode;

}
