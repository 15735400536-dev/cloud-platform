package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.ResetCycle;
import com.maxinhai.platform.enums.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "编码规则VO")
public class CodeRuleVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 规则编码
     */
    @ApiModelProperty(value = "规则编码")
    private String ruleCode;
    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String ruleName;
    /**
     * 编码前缀
     */
    @ApiModelProperty(value = "编码前缀")
    private String prefix;
    /**
     * 拼接日期标记（false:不拼接 true:拼接）
     */
    @ApiModelProperty(value = "拼接日期标记（false:不拼接 true:拼接）")
    private Boolean dateFlag;
    /**
     * 日期格式
     */
    @ApiModelProperty(value = "日期格式")
    private String dateFormat;
    /**
     * 序列号长度（生成时自动补零）
     */
    @ApiModelProperty(value = "序列号长度（生成时自动补零）")
    private Integer sequenceLength;
    /**
     * 序列号重置周期: DAY MONTH QUARTER YEAR NEVER
     */
    @ApiModelProperty(value = "序列号重置周期: DAY MONTH QUARTER YEAR NEVER")
    private ResetCycle resetCycle;
    /**
     * 上次重置时间
     */
    @ApiModelProperty(value = "上次重置时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastResetTime;
    /**
     * 当前序列号
     */
    @ApiModelProperty(value = "当前序列号")
    private Long currentSequence;
    /**
     * 状态（1:启用，0:禁用）
     */
    @ApiModelProperty(value = "状态（1:启用，0:禁用）")
    private Status status;

}
