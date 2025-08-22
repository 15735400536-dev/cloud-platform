package com.maxinhai.platform.vo.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "数据字典新增DTO")
public class OperationVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 工序编码
     */
    @ApiModelProperty(value = "工序编码")
    private String code;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String name;
    /**
     * 工序描述
     */
    @ApiModelProperty(value = "工序描述")
    private String description;
    /**
     * 标准工时
     */
    @ApiModelProperty(value = "标准工时")
    private BigDecimal workTime;
    /**
     * 状态：1.启用 0.禁用
     */
    @ApiModelProperty(value = "状态：1.启用 0.禁用")
    private Integer status;

}
