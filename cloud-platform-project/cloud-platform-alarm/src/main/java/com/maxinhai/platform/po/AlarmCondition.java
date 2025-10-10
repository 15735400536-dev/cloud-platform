package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("alarm_condition")
public class AlarmCondition extends RecordEntity {

    /**
     * 关联的规则ID
     */
    private String ruleId;
    /**
     * 条件字段
     */
    private String field;
    /**
     * 比较运算符（>、<、=、>=、<=、in、like）
     */
    private String condition;
    /**
     * 阈值（支持数值、字符串、列表）
     */
    private String value;

}
