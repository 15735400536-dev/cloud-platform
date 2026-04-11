package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.WarehouseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "仓库分页查询DTO")
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
    /**
     * 仓库类型：PT-普通总仓、LS-线边仓、YL-原料仓、CP-成品仓、BJ-备件仓、WH-危化仓
     */
    @ApiModelProperty(value = "仓库类型：PT-普通总仓、LS-线边仓、YL-原料仓、CP-成品仓、BJ-备件仓、WH-危化仓")
    private String type;

}
