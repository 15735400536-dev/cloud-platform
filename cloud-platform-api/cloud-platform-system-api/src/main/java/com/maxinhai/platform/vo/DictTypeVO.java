package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "字典类型VO")
public class DictTypeVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典标签
     */
    private String dictLabel;
    /**
     * 状态（1:启用，0:禁用）
     */
    private Integer status;

}
