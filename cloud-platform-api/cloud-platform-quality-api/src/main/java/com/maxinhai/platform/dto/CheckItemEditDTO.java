package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.ControlType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "检测项编辑DTO")
public class CheckItemEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 检测项编码
     */
    @ApiModelProperty(value = "检测项编码")
    private String itemCode;
    /**
     * 检测项名称
     */
    @ApiModelProperty(value = "检测项名称")
    private String itemName;
    /**
     * 控制类型: 定性、定量、手动输入
     */
    @ApiModelProperty(value = "控制类型: 定性、定量、手动输入")
    private ControlType controlType;
    /**
     * 最小值
     */
    @ApiModelProperty(value = "最小值")
    private BigDecimal minValue;
    /**
     * 最大值
     */
    @ApiModelProperty(value = "最大值")
    private BigDecimal maxValue;

}
