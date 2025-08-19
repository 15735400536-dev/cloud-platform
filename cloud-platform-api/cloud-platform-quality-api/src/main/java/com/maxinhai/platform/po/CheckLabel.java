package com.maxinhai.platform.po;

import com.maxinhai.platform.enums.CheckType;
import lombok.Data;

/**
 * @ClassName：CheckLabel
 * @Author: XinHai.Ma
 * @Date: 2025/8/19 15:02
 * @Description: 电子履历标签
 */
@Data
public class CheckLabel extends RecordEntity {

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
    /**
     * 检测类型: 自建、互检、专检
     */
    private CheckType checkType;
    /**
     * 检测项ID
     */
    private String checkItemId;

}
