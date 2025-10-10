package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.ProductVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "产品分页查询DTO")
public class ProductQueryDTO extends PageSearch<ProductVO> {

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
