package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.ApiWhitelistVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName：ApiWhitelistQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/9/12 14:41
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@Schema(description = "接口白名单-分页查询DTO")
public class ApiWhitelistQueryDTO extends PageSearch<ApiWhitelistVO> {

    /**
     * 模块/服务ID，例如：system
     */
    @Schema(description = "模块/服务ID，例如：system")
    private String serviceId;

}
