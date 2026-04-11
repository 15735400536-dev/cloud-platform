package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WarehouseAreaType {

    QC("QC", "待检区"),
    OK("OK", "良品区"),
    NG("NG", "不良品区"),
    RT("RT", "退货区"),
    RP("RP", "返修区"),
    BF("BF", "缓冲/周转区"),
    LS("LS", "线边补货区");

    @EnumValue
    private String code;
    @JsonValue
    private String name;

}
