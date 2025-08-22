package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "物料编辑DTO")
public class MaterialEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "物料编码")
    private String code;
    @ApiModelProperty(value = "物料名称")
    private String name;
    @ApiModelProperty(value = "物料类型ID")
    private String materialTypeId;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "物料描述")
    private String description;
    @ApiModelProperty(value = "规格")
    private String specs;
    /**
     * 图号
     */
    @ApiModelProperty(value = "图号")
    private String drawingNo;
    /**
     * 材质
     */
    @ApiModelProperty(value = "材质")
    private String material;
    @ApiModelProperty(value = "备注")
    private String remark;
}
