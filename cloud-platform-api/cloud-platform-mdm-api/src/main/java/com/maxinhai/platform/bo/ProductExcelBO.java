package com.maxinhai.platform.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "产品Excel导入BO")
public class ProductExcelBO {

    /**
     * 产品编码
     */
    @ExcelProperty(value = "产品编码")
    private String code;
    /**
     * 产品名称
     */
    @ExcelProperty(value = "产品名称")
    private String name;

}
