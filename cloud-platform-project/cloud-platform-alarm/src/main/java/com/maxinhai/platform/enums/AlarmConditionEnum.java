package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmConditionEnum {

    EQ("=", "等于"),
    NE("≠", "不等于"),
    GT(">", "大于"),
    GE("≥", "大于等于"),
    LT("<", "小于"),
    LE("≤", "小于等于"),;

    @EnumValue
    private String code;
    @JsonValue
    private String name;

}
