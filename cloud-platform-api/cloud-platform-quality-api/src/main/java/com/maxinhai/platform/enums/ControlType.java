package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 控制类型
 */
@Getter
@AllArgsConstructor
public enum ControlType {

    QL(1, "定性"),
    QT(2, "定量"),
    MI(3, "手动输入");
    @EnumValue
    private Integer key;
    @JsonValue
    private String value;

}
