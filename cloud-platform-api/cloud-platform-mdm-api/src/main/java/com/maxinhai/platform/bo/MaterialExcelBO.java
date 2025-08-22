package com.maxinhai.platform.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "物料Excel导入BO")
public class MaterialExcelBO {

    @ExcelProperty(value = "物料编码")
    private String code;
    @ExcelProperty(value = "物料名称")
    private String name;
    @ExcelProperty(value = "物料类型")
    private String materialType;
    @ExcelProperty(value = "单位")
    private String unit;
    @ExcelProperty(value = "规格")
    private String specs;
    @ExcelProperty(value = "图号")
    private String drawingNo;
    @ExcelProperty(value = "材质")
    private String material;
    @ExcelProperty(value = "备注")
    private String remark;

}
