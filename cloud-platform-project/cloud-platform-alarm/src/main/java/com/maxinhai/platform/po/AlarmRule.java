package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("alarm_rule")
public class AlarmRule extends RecordEntity {

    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 规则描述
     */
    private String ruleDesc;
    /**
     * 规则状态（0-禁用，1-启用）
     */
    private Integer status;
    /**
     * 规则优先级（1-高，2-中，3-低）
     */
    private Integer priority;

}
