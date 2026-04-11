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
@TableName("wms_warehouse")
public class Warehouse extends RecordEntity {

    /**
     * 仓库编码
     */
    private String code;
    /**
     * 仓库名称
     */
    private String name;
    /**
     * 仓库类型：PT-普通总仓、LS-线边仓、YL-原料仓、CP-成品仓、BJ-备件仓、WH-危化仓
     */
    private String type;
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    /**
     * 仓库地址
     */
    private String address;
    /**
     * 联系人
     */
    private String contactPerson;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 备注
     */
    private String remark;

    public static Warehouse build(WarehouseExcelBO excelBO) {
        Warehouse warehouse = new Warehouse();
        warehouse.setCode(excelBO.getWarehouseCode());
        warehouse.setName(excelBO.getWarehouseName());
        warehouse.setStatus(Status.Enable.getKey());
        warehouse.setAddress(excelBO.getAddress());
        warehouse.setContactPerson(excelBO.getContactPerson());
        warehouse.setContactPhone(excelBO.getContactPhone());
        return warehouse;
    }

}
