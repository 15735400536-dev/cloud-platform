package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 检测类型
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CheckType {

    ALL(-1, "全部"),
    SELF_CHECK(1, "自检"),
    MUTUAL_CHECK(2, "互检"),
    SPECIAL_CHECK(3, "专检");
    @EnumValue
    private Integer key;
    @JsonValue
    private String value;

}
