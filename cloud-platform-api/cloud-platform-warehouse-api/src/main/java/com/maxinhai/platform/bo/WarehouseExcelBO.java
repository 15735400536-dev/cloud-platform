package com.maxinhai.platform.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "仓库Excel导入BO")
public class WarehouseExcelBO {

    /**
     * 仓库
     */
    @ExcelProperty(value = "仓库编码")
    private String warehouseCode;
    @ExcelProperty(value = "仓库名称")
    private String warehouseName;
    @ExcelProperty(value = "仓库地址")
    private String address;
    @ExcelProperty(value = "联系人")
    private String contactPerson;
    @ExcelProperty(value = "联系电话")
    private String contactPhone;
    /**
     * 库区
     */
    @ExcelProperty(value = "库区编码")
    private String areaCode;
    @ExcelProperty(value = "库区名称")
    private String areaName;
    /**
     * 货架
     */
    @ExcelProperty(value = "货架编码")
    private String rackCode;
    @ExcelProperty(value = "货架名称")
    private String rackName;
    /**
     * 库位
     */
    @ExcelProperty(value = "库位编码")
    private String locationCode;
    @ExcelProperty(value = "库位名称")
    private String locationName;
    /**
     * 货位类型：1-普通，2-冷藏，3-冷冻等
     */
    @ExcelProperty(value = "货位类型：1-普通，2-冷藏，3-冷冻等")
    private Integer locationType;
    /**
     * 状态：0-禁用，1-启用
     */
    @ExcelProperty(value = "状态：0-禁用，1-启用")
    private Integer status;

}
