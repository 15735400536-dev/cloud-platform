package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据字典VO")
public class DataDictVO {

    @ApiModelProperty("主键ID")
    private String id;
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
