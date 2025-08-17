package com.maxinhai.platform.vo.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class BomVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * BOM编码
     */
    @ApiModelProperty("BOM编码")
    private String code;
    /**
     * BOM名称
     */
    @ApiModelProperty("BOM名称")
    private String name;
    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private String version;
    /**
     * BOM描述
     */
    @ApiModelProperty("BOM描述")
    private String description;
    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private String productId;
    @ApiModelProperty("产品编码")
    private String productCode;
    @ApiModelProperty("产品名称")
    private String productName;


}
