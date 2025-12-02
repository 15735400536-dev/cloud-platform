package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmStatus {
    INITIATE(1, "发起"),
    CANCEL(0, "取消");

    @EnumValue
    private final int code;
    @JsonValue
    private final String desc;

}
