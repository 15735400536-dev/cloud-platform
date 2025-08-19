package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 检测状态
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CheckStatus {

    NO(0, "待检测"),
    YES(1, "已检测");
    @EnumValue
    private Integer key;
    @JsonValue
    private String value;

}
