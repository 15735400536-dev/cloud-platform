package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.ResetCycle;
import com.maxinhai.platform.enums.Status;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_code_rule")
public class CodeRule extends RecordEntity {

    /**
     * 规则编码
     */
    private String ruleCode;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 编码前缀
     */
    private String prefix;
    /**
     * 拼接日期标记: false.不拼接 true.拼接
     */
    private Boolean dateFlag;
    /**
     * 日期格式
     */
    private String dateFormat;
    /**
     * 序列号长度（生成时自动补零）
     */
    private Integer sequenceLength;
    /**
     * 序列号重置周期: DAY MONTH QUARTER YEAR NEVER
     */
    private ResetCycle resetCycle;
    /**
     * 上次重置时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastResetTime;
    /**
     * 当前序列号
     */
    private Long currentSequence;
    /**
     * 状态（1:启用，0:禁用）
     */
    private Status status;

}
