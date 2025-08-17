package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum DelFlag {

    DELETED(1, "已删除"),
    NORMAL(0, "未删除");

    @EnumValue
    private int key;
    @JsonValue
    private String value;

}
