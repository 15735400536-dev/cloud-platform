package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum IssueStatus {

    SALE(1,"销售出库"),
    PRODUCTION(2,"生产领料"),
    TRANSFER(3,"调拨出库"),
    SCRAP(4,"报废出库");

    @EnumValue
    private Integer key;
    @JsonValue
    private String value;

}
