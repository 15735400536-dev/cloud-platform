package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum ConditionEnum {

    gt(">", "大于"),
    ge(">=", "大于等于"),
    lt("<", "小于"),
    le("<=", "小于等于"),
    eq("=", "等于"),
    ne("!=", "不等于"),
    in("", "在范围"),
    not_in("NOT IN", "不在范围"),
    like("LIKE", "全模糊查询"),
    like_left("LIKE", "左边模糊查询"),
    like_right("LIKE", "右边模糊查询"),
    ilike("ILIKE", "全模糊查询（不区分大小写）"),
    ilike_left("ILIKE", "左边模糊查询（不区分大小写）"),
    ilike_right("ILIKE", "右边模糊查询（不区分大小写）"),
    between("BETWEEN", "闭区间"),
    not_between("NOT BETWEEN","不在闭区间");
    @EnumValue
    private String key;
    @JsonValue
    private String value;

}
