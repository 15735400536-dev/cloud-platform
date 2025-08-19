package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.ProductVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class ProductQueryDTO extends PageSearch<ProductVO> {

    /**
     * 产品编码
     */
    @ApiModelProperty("产品编码")
    private String code;
    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String name;

}
