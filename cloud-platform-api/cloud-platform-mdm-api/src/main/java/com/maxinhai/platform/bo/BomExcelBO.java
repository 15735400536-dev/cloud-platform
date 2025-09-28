package com.maxinhai.platform.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class BomExcelBO {

    @ExcelProperty("产品编码")
    private String productCode;
    @ExcelProperty("版本号")
    private String version;
    @ExcelProperty("物料编码")
    private String materialCode;
    @ExcelProperty("物料数量")
    private BigDecimal qty;

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
    public static Map<ProductVersionKey, List<BomExcelBO>> groupByProductAndVersion(List<BomExcelBO> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return Map.of(); // 空列表返回空Map
        }

        // 分组核心逻辑：以ProductVersionKey为键，值为该组的所有BomExcelBO
        return dataList.stream()
                .collect(Collectors.groupingBy(
                        // 每个BomExcelBO映射为一个分组键
                        bo -> new ProductVersionKey(bo.getProductCode(), bo.getVersion())
                ));
    }

}
