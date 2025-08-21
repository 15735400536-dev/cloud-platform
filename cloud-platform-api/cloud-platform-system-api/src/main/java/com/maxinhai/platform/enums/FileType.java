package com.maxinhai.platform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型枚举
 */
@Getter
public enum FileType {

    WORD("word", "文档"),
    EXCEL("excel", "表格"),
    PDF("pdf", "PDF文档"),
    IMAGE("image", "图片"),
    VIDEO("video", "视频"),
    TEXT("text", "文本"),
    UNKNOWN("unknown", "未知");

    private final String code;
    private final String description;

    FileType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
