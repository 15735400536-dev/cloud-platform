package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class WarehouseLocationEditDTO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    /**
     * 库区ID
     */
    @ApiModelProperty("库区ID")
    private String areaId;
    /**
     * 货架ID
     */
    @ApiModelProperty("货架ID")
    private String rackId;
    /**
     * 货位编码
     */
    @ApiModelProperty("货位编码")
    private String code;
    /**
     * 货位名称
     */
    @ApiModelProperty("货位名称")
    private String name;
    /**
     * 货位类型：1-普通，2-冷藏，3-冷冻等
     */
    @ApiModelProperty("货位类型：1-普通，2-冷藏，3-冷冻等")
    private Integer locationType;
    /**
     * 状态：0-禁用，1-启用
     */
    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;

}
