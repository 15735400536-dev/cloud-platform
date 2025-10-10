package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.WarehouseRackVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "货架分页查询DTO")
public class WarehouseRackQueryDTO extends PageSearch<WarehouseRackVO> {

    /**
     * 货架编码
     */
    @ApiModelProperty(value = "货架编码")
    private String code;
    /**
     * 货架名称
     */
    @ApiModelProperty(value = "货架名称")
    private String name;

}
