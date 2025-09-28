package com.maxinhai.platform.bo;

import lombok.Data;

@Data
public class BomBO {

    private String id;
    /**
     * BOM编码
     */
    private String code;
    /**
     * BOM名称
     */
    private String name;
    /**
     * 版本号
     */
    private String version;
    /**
     * 产品ID
     */
    private String productId;
    private String productCode;
    private String productName;

}
