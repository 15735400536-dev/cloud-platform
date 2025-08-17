package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AuditStatus {

    DRAFT(0,"草稿"),
    WAIT(1,"待审核"),
    AUDITED(2,"已审核"),
    FINISH(3,"已完成"),
    CANCEL(4,"已取消");

    @EnumValue
    private Integer key;
    @JsonValue
    private String value;


}
