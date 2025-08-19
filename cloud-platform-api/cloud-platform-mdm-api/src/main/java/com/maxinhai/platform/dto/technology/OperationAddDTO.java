package com.maxinhai.platform.dto.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("数据字典新增DTO")
public class OperationAddDTO {

    /**
     * 工序编码
     */
    @ApiModelProperty("工序编码")
    private String code;
    /**
     * 工序名称
     */
    @ApiModelProperty("工序名称")
    private String name;
    /**
     * 工序描述
     */
    @ApiModelProperty("工序描述")
    private String description;
    /**
     * 标准工时
     */
    @ApiModelProperty("标准工时")
    private BigDecimal workTime;
    /**
     * 状态：1.启用 0.禁用
     */
    @ApiModelProperty("状态：1.启用 0.禁用")
    private Integer status;

}
