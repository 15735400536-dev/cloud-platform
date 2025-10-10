package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.DictTypeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "字典类型分页查询DTO")
public class DictTypeQueryDTO extends PageSearch<DictTypeVO> {

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型")
    private String dictType;
    /**
     * 字典标签
     */
    @ApiModelProperty(value = "字典标签")
    private String dictLabel;
    /**
     * 状态（1:启用，0:禁用）
     */
    @ApiModelProperty(value = "状态（1:启用，0:禁用）")
    private Integer status;

}
