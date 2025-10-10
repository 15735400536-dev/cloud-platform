package com.maxinhai.platform.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "工艺路线Excel导入BO")
public class RoutingExcelBO {

    @ExcelProperty(value = "产品编码")
    private String productCode;
    @ExcelProperty(value = "产品名称")
    private String productName;
    @ExcelProperty(value = "版本号")
    private String version;
    @ExcelProperty(value = "工序编码")
    private String operationCode;
    @ExcelProperty(value = "工序名称")
    private String operationName;
    @ExcelProperty(value = "标准工时")
    private BigDecimal workTime;
    @ExcelProperty(value = "工序描述")
    private String description;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductVersionKey {
        private String productCode; // 产品编码
        private String version;     // 版本号
    }

    /**
     * 对dataList按productCode和version分组
     * @param dataList
     * @return
     */
    public static Map<RoutingExcelBO.ProductVersionKey, List<RoutingExcelBO>> groupByProductAndVersion(List<RoutingExcelBO> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return Map.of(); // 空列表返回空Map
        }

        // 分组核心逻辑：以ProductVersionKey为键，值为该组的所有BomExcelBO
        return dataList.stream()
                .collect(Collectors.groupingBy(
                        // 每个BomExcelBO映射为一个分组键
                        bo -> new RoutingExcelBO.ProductVersionKey(bo.getProductCode(), bo.getVersion())
                ));
    }

}
