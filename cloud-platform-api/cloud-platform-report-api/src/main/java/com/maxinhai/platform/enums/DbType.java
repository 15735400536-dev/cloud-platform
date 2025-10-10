package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DbType {

    MySQL("MySQL", "MySQL"),
    PgSQL("PgSQL", "PgSQL"),
    Oracle("Oracle", "Oracle"),
    SqlServer("SqlServer", "SqlServer");

    @EnumValue
    private String key;
    @JsonValue
    private String value;
}
