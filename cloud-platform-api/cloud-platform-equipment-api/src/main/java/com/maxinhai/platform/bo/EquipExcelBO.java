package com.maxinhai.platform.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName：EquipExcelBO
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 16:23
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "设备Excel导入BO")
public class EquipExcelBO {

    /**
     * 设备编码
     */
    @ExcelProperty(value = "设备编码")
    private String equipCode;
    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称")
    private String equipName;
    /**
     * 设备类型
     */
    @ExcelProperty(value = "设备类型")
    private String equipType;
    /**
     * 设备型号
     */
    @ExcelProperty(value = "设备型号")
    private String model;
    /**
     * 设备规格
     */
    @ExcelProperty(value = "设备规格")
    private String specs;
    /**
     * 序列号/出厂编号 (唯一)
     */
    @ExcelProperty(value = "序列号")
    private String serialNo;
    /**
     * 资产编号
     */
    @ExcelProperty(value = "资产编号")
    private String assetNo;
    /**
     * 制造商
     */
    @ExcelProperty(value = "制造商")
    private String manufacturer;
    /**
     * 供应商
     */
    @ExcelProperty(value = "供应商")
    private String supplier;
    /**
     * 采购日期
     */
    @ExcelProperty(value = "采购日期")
    private Date purchaseDate;
    /**
     * 安装位置
     */
    @ExcelProperty(value = "安装位置")
    private String location;

}
