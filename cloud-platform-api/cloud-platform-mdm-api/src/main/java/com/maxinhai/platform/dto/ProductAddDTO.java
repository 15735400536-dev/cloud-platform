package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "产品新增DTO")
public class ProductAddDTO {

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private String code;
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String name;

}
