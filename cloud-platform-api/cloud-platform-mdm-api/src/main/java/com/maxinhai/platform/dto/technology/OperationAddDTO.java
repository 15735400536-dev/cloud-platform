package com.maxinhai.platform.dto.technology;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("数据字典新增DTO")
public class OperationAddDTO {

    /**
     * 工序编码
     */
    private String code;
    /**
     * 工序名称
     */
    private String name;
    /**
     * 工序描述
     */
    private String description;
    /**
     * 标准工时
     */
    private BigDecimal workTime;
    /**
     * 状态：1.启用 0.禁用
     */
    private Integer status;

}
