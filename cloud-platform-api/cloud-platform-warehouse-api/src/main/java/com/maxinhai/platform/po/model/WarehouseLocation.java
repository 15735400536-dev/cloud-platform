package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.bo.WarehouseExcelBO;
import com.maxinhai.platform.po.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 货位表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wms_location")
public class WarehouseLocation extends RecordEntity {

    /**
     * 仓库ID
     */
    private String warehouseId;
    /**
     * 库区ID
     */
    private String areaId;
    /**
     * 货架ID
     */
    private String rackId;
    /**
     * 货位编码
     */
    private String code;
    /**
     * 货位名称
     */
    private String name;
    /**
     * 货位类型：1-普通，2-冷藏，3-冷冻等
     */
    private Integer locationType;
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    public static WarehouseLocation build(WarehouseExcelBO excelBO) {
        WarehouseLocation location = new WarehouseLocation();
        location.setCode(excelBO.getLocationCode());
        location.setName(excelBO.getLocationName());
        location.setStatus(excelBO.getStatus());
        location.setLocationType(excelBO.getLocationType());
        return location;
    }

}
