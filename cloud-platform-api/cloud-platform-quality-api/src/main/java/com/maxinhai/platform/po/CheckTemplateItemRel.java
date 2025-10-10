package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("qc_check_template_item_rel")
public class CheckTemplateItemRel extends RecordEntity {

    /**
     * 检测模板ID
     */
    private String templateId;
    /**
     * 检测项ID
     */
    private String itemId;

}
