package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("字典类型编辑DTO")
public class DictTypeEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 字典类型
     */
    @ApiModelProperty("字典类型")
    private String dictType;
    /**
     * 字典标签
     */
    @ApiModelProperty("字典标签")
    private String dictLabel;
    /**
     * 状态（1:启用，0:禁用）
     */
    @ApiModelProperty("状态（1:启用，0:禁用）")
    private Integer status;

}
