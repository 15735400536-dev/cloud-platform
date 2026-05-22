package com.maxinhai.platform.po;

public class EmqxRule extends RecordEntity {

    /**
     * 规则ID
     */
    private String ruleId;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * EMQX规则SQL（核心，筛选MQTT消息的条件）
     */
    private String sql;
    /**
     *
     */
    private String baseUrl;
    /**
     * 是否启用规则
     */
    private Boolean enable;
    /**
     * 规则描述
     */
    private String description;
}
