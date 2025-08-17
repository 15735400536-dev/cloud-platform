package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 操作类型
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum OperateType {

    ALL(-1, "全部"),
    START(1, "开工"),
    PAUSE(2, "暂停"),
    RESUME(3, "复工"),
    REPORT(4, "报工");

    @EnumValue
    private Integer key;
    @JsonValue
    private String value;

}
