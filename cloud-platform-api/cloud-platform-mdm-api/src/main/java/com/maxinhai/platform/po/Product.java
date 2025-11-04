package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
