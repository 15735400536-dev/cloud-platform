package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.WarehouseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class WarehouseQueryDTO extends PageSearch<WarehouseVO> {

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String code;
    /**
     * 仓库名称
     */
    @ApiModelProperty(value = "仓库名称")
    private String name;

}
