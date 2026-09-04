package com.maxinhai.platform.bo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "产品配套BOM和工艺路线BO")
public class ProductBomAndRoutingBO {

    private String productId;
    private String productCode;
    private String productName;
    private String bomId;
    private String bomCode;
    private String bomName;
    private String bomVersion;
    private String routingId;
    private String routingCode;
    private String routingName;
    private String routingVersion;

}
