package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum EquipStatus {

    RUN(1,"运行"),
    STOP(2,"停机"),
    REPAIR(3,"维修"),
    SCRAP(4,"报废");

    @EnumValue
    private int key;
    @JsonValue
    private String value;

}
