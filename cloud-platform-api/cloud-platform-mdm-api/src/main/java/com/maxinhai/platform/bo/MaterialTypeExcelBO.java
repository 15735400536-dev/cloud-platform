package com.maxinhai.platform.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "物料类型Excel导入BO")
public class MaterialTypeExcelBO {

    @ExcelProperty(value = "物料类型编码")
    private String code;
    @ExcelProperty(value = "物料类型名称")
    private String name;
    @ExcelProperty(value = "物料类型描述")
    private String description;
    @ExcelProperty(value = "父级物料类型编码")
    private String parentCode;
    @ExcelProperty(value = "父级物料类型名称")
    private String parentName;

}
