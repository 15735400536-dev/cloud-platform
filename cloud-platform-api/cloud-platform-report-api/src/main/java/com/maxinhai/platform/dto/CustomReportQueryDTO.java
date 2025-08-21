package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.CustomReportVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：CustomReportQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:07
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel("DTO")
public class CustomReportQueryDTO extends PageSearch<CustomReportVO> {

    /**
     * 报表标识
     */
    private String key;
    /**
     * 报表标题
     */
    private String title;

}
