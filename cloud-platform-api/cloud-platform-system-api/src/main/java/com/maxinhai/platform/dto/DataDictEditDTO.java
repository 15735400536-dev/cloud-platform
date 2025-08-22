package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据字典编辑DTO")
public class DataDictEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    /**
     * 字典标识
     */
    @ApiModelProperty(value = "字典标识")
    private String dictKey;
    /**
     * 字典数值
     */
    @ApiModelProperty(value = "字典数值")
    private String dictValue;
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private Integer sort;
    /**
     * 状态（1:启用，0:禁用）
     */
    @ApiModelProperty(value = "状态（1:启用，0:禁用）")
    private Integer status;

}
