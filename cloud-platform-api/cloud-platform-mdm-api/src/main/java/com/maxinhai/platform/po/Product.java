package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("mdm_product")
public class Product extends RecordEntity {

    /**
     * 产品编码
     */
    private String code;
    /**
     * 产品名称
     */
    private String name;

}
