package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class ProductAddDTO {

    /**
     * 产品编码
     */
    private String code;
    /**
     * 产品名称
     */
    private String name;

}
