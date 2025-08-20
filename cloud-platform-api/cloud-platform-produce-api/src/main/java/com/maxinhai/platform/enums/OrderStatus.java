package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {

    INIT(0, "初始"),
    START(1, "开工"),
    PAUSE(2, "暂停"),
    RESUME(3, "复工"),
    REPORT(4, "报工");

    @EnumValue
    private Integer key;
    @JsonValue
    private String value;

}
