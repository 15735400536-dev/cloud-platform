package com.maxinhai.platform.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 媒体类型
 */
public enum MediaType {

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
    MediaType(String mediaType) { this.mediaType = mediaType; }
    public String getMediaType() { return mediaType; }

}
