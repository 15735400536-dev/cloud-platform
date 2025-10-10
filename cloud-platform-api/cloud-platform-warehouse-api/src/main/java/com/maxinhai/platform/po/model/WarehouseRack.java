package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.bo.WarehouseExcelBO;
import com.maxinhai.platform.enums.Status;
import com.maxinhai.platform.po.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 货架表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wms_race")
public class WarehouseRack extends RecordEntity {

    /**
     * 仓库ID
     */
    private String warehouseId;
    /**
     * 库区ID
     */
    private String areaId;
    /**
     * 货架编码
     */
    private String code;
    /**
     * 货架名称
     */
    private String name;
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    public static WarehouseRack build(WarehouseExcelBO excelBO) {
        WarehouseRack rack = new WarehouseRack();
        rack.setCode(excelBO.getRackCode());
        rack.setName(excelBO.getRackName());
        rack.setStatus(Status.Enable.getKey());
        return rack;
    }

}
