package com.maxinhai.platform.enums;

/**
 * 告警条件枚举类
 * 涵盖巡检机器人算法结果判断的所有告警条件类型
 *
 * @author 你的名称
 * @date 2026-01-13
 */
public enum AlarmConditionEnum {

    /**
     * 大于
     */
    GT("GT", "大于"),

    /**
     * 大于等于
     */
    GE("GE", "大于等于"),

    /**
     * 小于
     */
    LT("LT", "小于"),

    /**
     * 小于等于
     */
    LE("LE", "小于等于"),

    /**
     * 等于
     */
    EQ("EQ", "等于"),

    /**
     * 不等于
     */
    NE("NE", "不等于"),

    /**
     * 在范围（闭区间，包含边界值）
     */
    IN_RANGE("IN_RANGE", "在范围"),

    /**
     * 不在范围（闭区间，不包含边界值）
     */
    NOT_IN_RANGE("NOT_IN_RANGE", "不在范围");

    /**
     * 条件编码（存储到数据库的字段值）
     */
    private final String code;

    /**
     * 条件中文描述（前端展示/日志输出用）
     */
    private final String desc;

    /**
     * 构造方法
     * @param code 条件编码
     * @param desc 中文描述
     */
    AlarmConditionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // ========== 通用获取方法 ==========

    /**
     * 获取条件编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取中文描述
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 根据编码匹配枚举（常用，比如从数据库取值后转换）
     * @param code 条件编码（如"GT"、"IN_RANGE"）
     * @return 对应的枚举常量，无匹配则返回null
     */
    public static AlarmConditionEnum getByCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        for (AlarmConditionEnum condition : AlarmConditionEnum.values()) {
            if (condition.getCode().equals(code)) {
                return condition;
            }
        }
        return null;
    }

    /**
     * 根据中文描述匹配枚举（可选，用于前端传描述时转换）
     * @param desc 中文描述（如"大于"、"在范围"）
     * @return 对应的枚举常量，无匹配则返回null
     */
    public static AlarmConditionEnum getByDesc(String desc) {
        if (desc == null || desc.isEmpty()) {
            return null;
        }
        for (AlarmConditionEnum condition : AlarmConditionEnum.values()) {
            if (condition.getDesc().equals(desc)) {
                return condition;
            }
        }
        return null;
    }

}
