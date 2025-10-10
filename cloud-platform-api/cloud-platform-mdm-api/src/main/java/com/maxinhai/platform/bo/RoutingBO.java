package com.maxinhai.platform.bo;

import lombok.Data;

@Data
public class RoutingBO {

    /**
     * 工艺路线编码
     */
    private String code;
    /**
     * 工艺路线名称
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
