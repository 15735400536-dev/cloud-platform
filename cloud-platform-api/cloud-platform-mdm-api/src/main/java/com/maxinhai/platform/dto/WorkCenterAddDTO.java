package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据字典新增DTO")
public class WorkCenterAddDTO {

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
