package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.WorkCenterVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "加工中心分页查询DTO")
public class WorkCenterQueryDTO extends PageSearch<WorkCenterVO> {

    /**
     * 加工中心编码
     */
    @ApiModelProperty(value = "加工中心编码")
    private String code;
    /**
     * 加工中心名称
     */
    @ApiModelProperty(value = "加工中心名称")
    private String name;

}
