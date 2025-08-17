package com.maxinhai.platform.po.technology;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("mdm_bom_detail")
public class BomDetail extends RecordEntity {

    private String bomId;
    private String materialId;
    private BigDecimal qty;
    private String parentId;

}
