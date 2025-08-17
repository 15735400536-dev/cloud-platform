package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.WarehouseRackVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class WarehouseRackQueryDTO extends PageSearch<WarehouseRackVO> {

    /**
     * 货架编码
     */
    @ApiModelProperty("货架编码")
    private String code;
    /**
     * 货架名称
     */
    @ApiModelProperty("货架名称")
    private String name;

}
