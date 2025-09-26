package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.SupplierVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：SupplierQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/9/26 15:59
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "供应商分页查询DTO")
public class SupplierQueryDTO extends PageSearch<SupplierVO> {

    /**
     * 供应商名称（企业名称，如 “XX 电子元件厂”）
     */
    private String name;
    /**
     * 供应商类型（如 “原材料”“设备”“服务”“软件”）
     */
    private Integer type;

}
