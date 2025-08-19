package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.CheckType;
import com.maxinhai.platform.po.CheckLabel;
import com.maxinhai.platform.vo.CheckLabelVO;
import com.microsoft.schemas.office.visio.x2012.main.impl.PagesDocumentImpl;
import lombok.Data;

/**
 * @ClassName：CheckLabelQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/19 16:25
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
public class CheckLabelQueryDTO extends PageSearch<CheckLabelVO> {

    /**
     * 产品ID
     */
    private String productId;
    /**
     * 检测类型: 自建、互检、专检
     */
    private CheckType checkType;

}
