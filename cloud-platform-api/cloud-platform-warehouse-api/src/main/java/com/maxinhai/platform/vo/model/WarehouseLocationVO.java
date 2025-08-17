package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("VO")
public class WarehouseLocationVO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
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
    /**
     * 仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库编码")
    private String warehouseCode;
    @ApiModelProperty("仓库名称")
    private String warehouseName;
    /**
     * 库区ID
     */
    @ApiModelProperty("库区ID")
    private String areaId;
    @ApiModelProperty("库区编码")
    private String areaCode;
    @ApiModelProperty("库区名称")
    private String areaName;
    /**
     * 货架ID
     */
    @ApiModelProperty("货架ID")
    private String rackId;
    @ApiModelProperty("货架编码")
    private String rackCode;
    @ApiModelProperty("货架名称")
    private String rackName;

}
