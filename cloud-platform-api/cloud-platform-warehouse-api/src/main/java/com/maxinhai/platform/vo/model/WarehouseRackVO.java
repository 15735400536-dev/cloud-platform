package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "VO")
public class WarehouseRackVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "货架编码")
    private String code;
    @ApiModelProperty(value = "货架名称")
    private String name;
    /**
     * 状态：0-禁用，1-启用
     */
    @ApiModelProperty(value = "状态：0-禁用，1-启用")
    private Integer status;
    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;
    /**
     * 库区ID
     */
    @ApiModelProperty(value = "库区ID")
    private String areaId;
    @ApiModelProperty(value = "库区编码")
    private String areaCode;
    @ApiModelProperty(value = "库区名称")
    private String areaName;

}
