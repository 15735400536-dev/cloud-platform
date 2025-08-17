package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.enums.EquipStatus;
import lombok.Data;

import java.util.Date;

@Data
@TableName("dm_equip")
public class Equipment extends RecordEntity {

    /**
     * 设备编码
     */
    private String equipCode;
    /**
     * 设备名称
     */
    private String equipName;
    /**
     * 设备类型
     */
    private String equipType;
    /**
     * 设备型号
     */
    private String model;
    /**
     * 设备规格
     */
    private String specs;
    /**
     * 序列号/出厂编号 (唯一)
     */
    private String serialNo;
    /**
     * 资产编号
     */
    private String assetNo;
    /**
     * 制造商
     */
    private String manufacturer;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 采购日期
     */
    private Date purchaseDate;
    /**
     * 安装位置
     */
    private String location;
    /**
     * 设备状态
     */
    private EquipStatus status;

}
