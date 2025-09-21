package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.bo.WarehouseExcelBO;
import com.maxinhai.platform.enums.Status;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

/**
 * 库区表
 */
@Data
@TableName("wms_area")
public class WarehouseArea extends RecordEntity {

    /**
     * 仓库ID
     */
    private String warehouseId;
    /**
     * 库区编码
     */
    private String code;
    /**
     * 库区名称
     */
    private String name;
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

    public static WarehouseArea build(WarehouseExcelBO excelBO) {
        WarehouseArea area = new WarehouseArea();
        area.setCode(excelBO.getAreaCode());
        area.setName(excelBO.getAreaName());
        area.setStatus(Status.Enable.getKey());
        return area;
    }

}
