package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 库存流水操作类型
 */
@Getter
@AllArgsConstructor
public enum OperateType {

    ISSUE(1,"入库"),
    RECEIPT(2,"出库"),
    INVENTORY_ADJUSTMENT(3,"库存调整"),
    TRANSFER_ISSUE(4,"调拨入库"),
    TRANSFER_RECEIPT(5,"调拨出库");

    @EnumValue
    private Integer key;
    @JsonValue
    private String value;

}
