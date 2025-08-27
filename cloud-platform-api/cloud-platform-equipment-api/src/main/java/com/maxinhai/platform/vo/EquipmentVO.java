package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "VO")
public class EquipmentVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 设备编码
     */
    @ApiModelProperty("设备编码")
    private String equipCode;
    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipName;
    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    private String equipType;
    /**
     * 设备型号
     */
    @ApiModelProperty("设备型号")
    private String model;
    /**
     * 设备规格
     */
    @ApiModelProperty("设备规格")
    private String specs;
    /**
     * 序列号/出厂编号 (唯一)
     */
    @ApiModelProperty("序列号/出厂编号 (唯一)")
    private String serialNo;
    /**
     * 资产编号
     */
    @ApiModelProperty("资产编号")
    private String assetNo;
    /**
     * 制造商
     */
    @ApiModelProperty("制造商")
    private String manufacturer;
    /**
     * 供应商
     */
    @ApiModelProperty("供应商")
    private String supplier;
    /**
     * 采购日期
     */
    @ApiModelProperty("采购日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date purchaseDate;
    /**
     * 安装位置
     */
    @ApiModelProperty("安装位置")
    private String location;

}
