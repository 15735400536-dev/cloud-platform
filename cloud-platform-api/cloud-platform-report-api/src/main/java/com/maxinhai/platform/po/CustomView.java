package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.enums.ChartType;
import lombok.Data;

/**
 * @ClassName：CustomView
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 16:21
 * @Description: 自定义视图
 */
@Data
@TableName("rep_view")
public class CustomView extends RecordEntity {

    /**
     * 视图标识
     */
    private String key;
    /**
     * 视图标题
     */
    private String title;
    /**
     * 图表类型
     */
    private ChartType type;
    /**
     * 查询SQL
     */
    private String sqlId;
    /**
     * 报表ID
     */
    @TableField(exist = false)
    private String reportId;

}
