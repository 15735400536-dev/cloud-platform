package com.maxinhai.platform.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("VO")
public class WarehouseRackVO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("货架编码")
    private String code;
    @ApiModelProperty("货架名称")
    private String name;
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

}
