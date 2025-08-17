package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

/**
 * 库区表
 */
@Data
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

}
