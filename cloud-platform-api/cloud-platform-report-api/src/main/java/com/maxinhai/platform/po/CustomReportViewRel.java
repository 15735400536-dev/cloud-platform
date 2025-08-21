package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName：CustomReportViewRel
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 17:23
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@TableName("rep_report_view_rel")
public class CustomReportViewRel extends RecordEntity {

    /**
     * 报表ID
     */
    private String reportId;
    /**
     * 视图ID
     */
    private String viewId;

}
