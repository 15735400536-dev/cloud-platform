package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("物料新增DTO")
public class MaterialAddDTO {

    @ApiModelProperty("物料编码")
    private String code;
    @ApiModelProperty("物料名称")
    private String name;
    @ApiModelProperty("物料类型ID")
    private String materialTypeId;
    @ApiModelProperty("单位")
    private String unit;
    @ApiModelProperty("物料描述")
    private String description;
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
