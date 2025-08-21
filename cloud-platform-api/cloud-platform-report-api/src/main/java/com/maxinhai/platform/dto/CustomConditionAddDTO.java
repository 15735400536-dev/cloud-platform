package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.ConditionEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @ClassName：CustomConditionAddDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:09
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel("DTO")
public class CustomConditionAddDTO {

    /**
     * 查询字段
     */
    private String field;
    /**
     * 条件：大于、大于等于、小于、小于等于、等于、不等于、在范围、不在范围
     */
    private ConditionEnum condition;
    /**
     * 下限
     */
    private String minVal;
    /**
     * 上限
     */
    private String maxVal;
    /**
     * 标准数值
     */
    private String standardVal;
    /**
     * 范围
     */
    private List<String> range;
    /**
     * 查询SQL
     */
    private String sqlId;

}
