package com.maxinhai.platform.vo;

import com.maxinhai.platform.enums.CheckType;
import lombok.Data;

/**
 * @ClassName：CheckLabelVO
 * @Author: XinHai.Ma
 * @Date: 2025/8/19 16:24
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
public class CheckLabelVO {

    /**
     * 标签编码
     */
    private String code;
    /**
     * 标签名称
     */
    private String name;
    /**
     * 产品ID
     */
    private String productId;
    private String productCode;
    private String productName;
    /**
     * 检测类型: 自建、互检、专检
     */
    private CheckType checkType;
    /**
     * 检测项ID
     */
    private String checkItemId;

}
