package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChartType {

    TABLE("table", "表格"),
    PIE_CHART("pie", "饼图"),
    BAR_CHART("bar", "柱状图"),
    LINE_CHART("line", "折线图");

    @EnumValue
    private String key;
    @JsonValue
    private String value;

}
