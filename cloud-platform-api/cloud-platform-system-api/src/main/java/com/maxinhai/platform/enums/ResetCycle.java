package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 重置周期
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResetCycle {

    DAY(1, "天"),
    WEEK(2, "周"),
    MONTH(3, "月"),
    QUARTER(4, "季度"),
    YEAR(5, "年"),
    NEVER(6, "从不");

    @EnumValue
    private Integer key;
    @JsonValue
    private String value;

}
