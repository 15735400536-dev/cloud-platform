package com.maxinhai.platform.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 新闻状态枚举类
 */
@Getter
public enum NewsStatusEnum {

    DRAFT(0, "草稿"),
    PENDING_AUDIT(1, "待审核"),
    AUDIT_PASSED(2, "审核通过"),
    PUBLISHED(3, "已发布"),
    REJECTED(4, "已驳回");

    // 数据库存储的数值
    private final int code;
    // 状态描述（前端展示用）
    private final String desc;

    NewsStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 提供根据code获取枚举的方法
    public static NewsStatusEnum getByCode(int code) {
        for (NewsStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态码: " + code);
    }

    @JsonValue // 序列化时返回code（前端接收的是数值）
    public int getCodeForJson() {
        return code;
    }
}
