package com.maxinhai.platform.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "工序Excel导入BO")
public class OperationExcelBO {

    /**
     * 工序编码
     */
    @ExcelProperty(value = "工序编码")
    private String code;
    /**
     * 工序名称
     */
    @ExcelProperty(value = "工序名称")
    private String name;
    /**
     * 工序描述
     */
    @ExcelProperty(value = "工序描述")
    private String description;
    /**
     * 是否关键工序
     */
    @ExcelProperty(value = "关键工序")
    private String isKey;
    /**
     * 准备工时(分钟)
     */
    @ExcelProperty(value = "准备工时(分钟)")
    private BigDecimal setupTime;
    /**
     * 单件工时/标准工时(分钟)
     */
    @ExcelProperty(value = "单件工时(分钟)")
    private BigDecimal workTime;

}
