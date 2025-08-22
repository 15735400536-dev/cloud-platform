package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class WarehouseRackAddDTO {

    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    /**
     * 库区ID
     */
    @ApiModelProperty(value = "库区ID")
    private String areaId;
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
    /**
     * 状态：0-禁用，1-启用
     */
    @ApiModelProperty(value = "状态：0-禁用，1-启用")
    private Integer status;

}
