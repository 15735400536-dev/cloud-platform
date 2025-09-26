package com.maxinhai.platform.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.maxinhai.platform.po.technology.Routing;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "工艺路线Excel导入BO")
public class RoutingExcelBO {

    @ExcelProperty(value = "产品编码")
    private String productCode;
    @ExcelProperty(value = "产品名称")
    private String productName;
    @ExcelProperty(value = "工艺路线编码")
    private String routingCode;
    @ExcelProperty(value = "工艺路线名称")
    private String routingName;
    @ExcelProperty(value = "工序编码")
    private String operationCode;
    @ExcelProperty(value = "工序名称")
    private String operationName;
    @ExcelProperty(value = "工序描述")
    private String description;
    @ExcelProperty(value = "标准工时")
    private BigDecimal workTime;

    public static Routing build(RoutingExcelBO excel) {
        Routing routing = new Routing();
        // TODO
        routing.setStatus(1);
        return routing;
    }

}
