package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 媒体类型
 */
public enum ContentType {

//    // 纯文本（无格式）
//    TEXT_PLAIN,
//    // HTML 文本
//    TEXT_HTML,
//    // JSON 格式数据（标准）
//    APPLICATION_JSON,
//    // 表单数据（键值对，URL 编码）
//    APPLICATION_FORM_URLENCODED,
//
//    // 多部分表单数据（支持文件 + 普通字段）
//    MULTIPART_FORM_DATA,
//    // XML 格式数据
//    APPLICATION_XML,
//    // JPEG/PNG 格式图片
//    IMAGE_JPEG,
//    //JPEG/PNG 格式图片
//    IMAGE_PNG,
//    // 二进制流数据（通用二进制格式）
//    APPLICATION_OCTET_STREAM,

    APPLICATION_JSON("application/json; charset=utf-8"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded; charset=utf-8"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    TEXT_PLAIN("text/plain; charset=utf-8"),
    TEXT_HTML("text/html; charset=utf-8"),
    APPLICATION_XML("application/xml; charset=utf-8"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    APPLICATION_OCTET_STREAM("application/octet-stream");

    @EnumValue
    @JsonValue
    private final String mediaType;
    ContentType(String mediaType) { this.mediaType = mediaType; }
    public String getMediaType() { return mediaType; }

}
