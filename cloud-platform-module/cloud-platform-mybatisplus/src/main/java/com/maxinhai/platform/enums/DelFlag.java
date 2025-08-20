package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DelFlag {

    DELETED(1, "已删除"),
    NORMAL(0, "未删除");

    @EnumValue
    private final int key;
    @JsonValue
    private final String value;

}
