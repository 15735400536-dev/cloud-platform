package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReceiptStatus {

    PURCHASE(1,"采购入库"),
    PRODUCTION(2,"生产入库"),
    RETURN(3,"退货入库"),
    TRANSFER(4,"调拨入库");

    @EnumValue
    private Integer key;
    @JsonValue
    private String value;

}
