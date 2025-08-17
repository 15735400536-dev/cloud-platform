package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class DataDictAddDTO {

    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典标识
     */
    private String dictKey;
    /**
     * 字典数值
     */
    private String dictValue;
    /**
     * 排序字段
     */
    private Integer sort;
    /**
     * 状态（1:启用，0:禁用）
     */
    private Integer status;

}
