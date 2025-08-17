package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("字典类型新增DTO")
public class DictTypeAddDTO {

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
