package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName：CustomView
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 16:21
 * @Description: 自定义报表
 */
@Data
@TableName("rep_report")
public class CustomReport extends RecordEntity {

    /**
     * 报表标识
     */
    private String key;
    /**
     * 报表标题
     */
    private String title;

}
