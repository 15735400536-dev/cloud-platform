package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class MaterialVO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("物料编码")
    private String code;
    @ApiModelProperty("物料名称")
    private String name;
    @ApiModelProperty("物料类型ID")
    private String materialTypeId;
    @ApiModelProperty("物料类型编码")
    private String materialTypeCode;
    @ApiModelProperty("物料类型名称")
    private String materialTypeName;
    @ApiModelProperty("计量单位")
    private String unit;
    @ApiModelProperty("物料描述")
    private String description;
    @ApiModelProperty("型号")
    private String model;
    @ApiModelProperty("规格")
    private String specs;
    /**
     * 图号
     */
    @ApiModelProperty("图号")
    private String drawingNo;
    /**
     * 材质
     */
    @ApiModelProperty("材质")
    private String material;
    @ApiModelProperty("备注")
    private String remark;

}
