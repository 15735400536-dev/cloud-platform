package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "库位新增DTO")
public class WarehouseLocationAddDTO {

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
     * 货架ID
     */
    @ApiModelProperty(value = "货架ID")
    private String rackId;
    /**
     * 货位编码
     */
    @ApiModelProperty(value = "货位编码")
    private String code;
    /**
     * 货位名称
     */
    @ApiModelProperty(value = "货位名称")
    private String name;
    /**
     * 货位类型：1-普通，2-冷藏，3-冷冻等
     */
    @ApiModelProperty(value = "货位类型：1-普通，2-冷藏，3-冷冻等")
    private Integer locationType;
    /**
     * 状态：0-禁用，1-启用
     */
    @ApiModelProperty(value = "状态：0-禁用，1-启用")
    private Integer status;

}
