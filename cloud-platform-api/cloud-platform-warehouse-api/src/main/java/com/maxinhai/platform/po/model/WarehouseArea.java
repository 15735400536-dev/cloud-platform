package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.bo.WarehouseExcelBO;
import com.maxinhai.platform.enums.Status;
import com.maxinhai.platform.po.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库区表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
     * 库区类型：01-良品区、02-待检区、03-不良品区、04-退货区、05-返修区、06-缓冲/周转区、07-线边补货区
     */
    private String type;
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
