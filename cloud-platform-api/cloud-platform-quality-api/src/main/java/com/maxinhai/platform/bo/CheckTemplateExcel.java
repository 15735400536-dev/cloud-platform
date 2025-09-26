package com.maxinhai.platform.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.maxinhai.platform.enums.CheckType;
import com.maxinhai.platform.enums.ControlType;
import com.maxinhai.platform.po.CheckItem;
import com.maxinhai.platform.po.CheckTemplate;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName：CheckTemplateExcel
 * @Author: XinHai.Ma
 * @Date: 2025/9/26 13:54
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
public class CheckTemplateExcel {

    /**
     * 检测模板编码
     */
    @ExcelProperty("检测模板编码")
    private String templateCode;
    /**
     * 检测模板名称
     */
    @ExcelProperty("检测模板名称")
    private String templateName;
    /**
     * 检测类型: 自建、互检、专检
     */
    @ExcelProperty("检测类型: 自建、互检、专检")
    private CheckType checkType;
    /**
     * 产品编码
     */
    @ExcelProperty("产品编码")
    private String productCode;
    /**
     * 工序编码
     */
    @ExcelProperty("工序编码")
    private String operationCode;
    /**
     * 检测项编码
     */
    @ExcelProperty("检测项编码")
    private String itemCode;
    /**
     * 检测项名称
     */
    @ExcelProperty("检测项名称")
    private String itemName;
    /**
     * 控制类型: 定性、定量、手动输入
     */
    @ExcelProperty("控制类型: 定性、定量、手动输入")
    private ControlType controlType;
    /**
     * 最小值
     */
    @ExcelProperty("最小值")
    private BigDecimal minValue;
    /**
     * 最大值
     */
    @ExcelProperty("最大值")
    private BigDecimal maxValue;

    public static CheckTemplate buildCheckTemplate(CheckTemplateExcel excel) {
        CheckTemplate template = new CheckTemplate();
        template.setTemplateCode(excel.getTemplateCode());
        template.setTemplateName(excel.getTemplateName());
        template.setCheckType(excel.getCheckType());
        return template;
    }

    public static CheckItem buildCheckItem(CheckTemplateExcel excel) {
        CheckItem item = new CheckItem();
        item.setItemCode(excel.getItemCode());
        item.setItemName(excel.getItemName());
        item.setControlType(excel.controlType);
        item.setMinValue(excel.getMinValue());
        item.setMaxValue(excel.getMaxValue());
        return item;
    }

}
