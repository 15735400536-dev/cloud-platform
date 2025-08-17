package com.maxinhai.platform.po.technology;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("mdm_operation")
public class Operation extends RecordEntity {

    /**
     * 工序编码
     */
    private String code;
    /**
     * 工序名称
     */
    private String name;
    /**
     * 工序描述
     */
    private String description;
    /**
     * 标准工时
     */
    private BigDecimal workTime;
    /**
     * 状态：1.启用 0.禁用
     */
    private Integer status;

}
