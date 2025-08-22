package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.DataDictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据字典分页查询DTO")
public class DataDictQueryDTO extends PageSearch<DataDictVO> {

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型")
    private String dictType;

}
