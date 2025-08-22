package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.CustomConditionVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：CustomConditionQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:09
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "查询条件分页查询DTO")
public class CustomConditionQueryDTO extends PageSearch<CustomConditionVO> {

    /**
     * 查询字段
     */
    private String field;

}
