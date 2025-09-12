package com.maxinhai.platform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName：ApiWhitelistAddDTO
 * @Author: XinHai.Ma
 * @Date: 2025/9/12 14:42
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@Schema(description = "接口白名单-新增DTO")
public class ApiWhitelistAddDTO {

    /**
     * 模块/服务ID，例如：system
     */
    @Schema(description = "模块/服务ID，例如：system")
    private String serviceId;
    /**
     * 接口地址，例如：/api/login
     */
    @Schema(description = "接口地址，例如：/api/login")
    private String apiUrl;
    /**
     * 是否限制IP访问：false.不限制 true.限制
     */
    @Schema(description = "是否限制IP访问：false.不限制 true.限制")
    private Boolean ipFlag;
    /**
     * IP白名单，多个IP地址用“,”隔开
     */
    @Schema(description = "IP白名单，多个IP地址用“,”隔开")
    private String ipWhitelist;
    /**
     * 是否限时开放：false.不限时 true.限时
     */
    @Schema(description = "是否限时开放：false.不限时 true.限时")
    private Boolean timeFlag;
    /**
     * 生效时间
     */
    @Schema(description = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    /**
     * 失效时间
     */
    @Schema(description = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 状态：0.禁用 1.启用
     */
    @Schema(description = "状态：0.禁用 1.启用")
    private Integer status;

}
