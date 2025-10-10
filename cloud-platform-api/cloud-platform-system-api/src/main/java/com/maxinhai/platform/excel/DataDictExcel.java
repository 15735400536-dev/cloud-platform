package com.maxinhai.platform.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.maxinhai.platform.po.DataDict;
import com.maxinhai.platform.po.DictType;
import lombok.Data;

/**
 * @ClassName：DataDictExcel
 * @Author: XinHai.Ma
 * @Date: 2025/9/25 10:23
 * @Description: 数据字典Excel
 */
@Data
public class DataDictExcel {

    /**
     * 字典类型
     */
    @ExcelProperty(value = "字典类型")
    private String dictType;
    /**
     * 字典标签
     */
    @ExcelProperty(value = "字典标签")
    private String dictLabel;
    /**
     * 字典标识
     */
    @ExcelProperty(value = "字典标识")
    private String dictKey;
    /**
     * 字典数值
     */
    @ExcelProperty(value = "字典数值")
    private String dictValue;
    /**
     * 排序字段
     */
    @ExcelProperty(value = "排序字段")
    private Integer sort;

    public static DictType buildDictType(DataDictExcel excel) {
        DictType type = new DictType();
        type.setDictType(excel.getDictType());
        type.setDictLabel(excel.getDictLabel());
        type.setStatus(1);
        return type;
    }

    public static DataDict buildDataDict(DataDictExcel excel) {
        DataDict dataDict = new DataDict();
        dataDict.setDictType(excel.getDictType());
        dataDict.setDictKey(excel.getDictKey());
        dataDict.setDictValue(excel.getDictValue());
        dataDict.setSort(excel.getSort());
        dataDict.setStatus(1);
        return dataDict;
    }

}
