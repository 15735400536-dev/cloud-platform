package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("加工中心编辑DTO")
public class WorkCenterEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 加工中心编码
     */
    @ApiModelProperty("加工中心编码")
    private String code;
    /**
     * 加工中心名称
     */
    @ApiModelProperty("加工中心名称")
    private String name;

}
