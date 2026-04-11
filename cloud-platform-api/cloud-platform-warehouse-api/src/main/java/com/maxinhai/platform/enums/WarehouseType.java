package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WarehouseType {

    PT("PT", "普通总仓"),
    LS("LS", "线边仓"),
    YL("YL", "原料仓"),
    CP("CP", "成品仓"),
    BJ("BJ", "备件仓"),
    WH("WH", "危化仓");

    @EnumValue
    private String code;
    @JsonValue
    private String name;

}
