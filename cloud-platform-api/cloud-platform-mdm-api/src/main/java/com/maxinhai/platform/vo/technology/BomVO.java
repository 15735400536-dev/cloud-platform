package com.maxinhai.platform.vo.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "物料BOM VO")
public class BomVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * BOM编码
     */
    @ApiModelProperty(value = "BOM编码")
    private String code;
    /**
     * BOM名称
     */
    @ApiModelProperty(value = "BOM名称")
    private String name;
    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;
    /**
     * BOM描述
     */
    @ApiModelProperty(value = "BOM描述")
    private String description;
    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productId;
    @ApiModelProperty(value = "产品编码")
    private String productCode;
    @ApiModelProperty(value = "产品名称")
    private String productName;


}
