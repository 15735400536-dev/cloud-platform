package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.BomBO;
import com.maxinhai.platform.bo.BomExcelBO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.BomMapper;
import com.maxinhai.platform.mapper.MaterialMapper;
import com.maxinhai.platform.mapper.ProductMapper;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.po.technology.BomDetail;
import com.maxinhai.platform.service.BomDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BomExcelListener implements ReadListener<BomExcelBO> {

    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private BomMapper bomMapper;
    @Resource
    private BomDetailService bomDetailService;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private List<BomExcelBO> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(BomExcelBO data, AnalysisContext context) {
        log.info("读取到数据: {}", data);
        dataList.add(data);

        // 达到BATCH_COUNT，需要去存储一次数据库，防止数据量太大，内存溢出
        if (dataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            dataList.clear();
        }
    }

    /**
     * 所有数据读取完成后调用该方法
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也被存储
        saveData();
        // 存储完成清理 list
        dataList.clear();
        log.info("所有数据解析完成！");
    }

    /**
     * 保存数据到数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveData() {
        // 没有内容不执行后面操作
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        log.info("开始保存 {} 条数据到数据库", dataList.size());

        // 对dataList按productCode和version分组
        Map<BomExcelBO.ProductVersionKey, List<BomExcelBO>> keyListMap = BomExcelBO.groupByProductAndVersion(dataList);
        // 查询产品信息
        Set<String> productCodeSet = dataList.stream().map(BomExcelBO::getProductCode).collect(Collectors.toSet());

        // 校验BOM是否存在
        List<BomBO> bomList = bomMapper.selectJoinList(BomBO.class, new MPJLambdaWrapper<Bom>()
                .innerJoin(Product.class, Product::getId, Bom::getProductId)
                .in(Product::getCode, productCodeSet)
                .select(Bom::getVersion, Bom::getProductId)
                .select(Product::getCode, Product::getName)
                .selectAll(Bom.class)
                .selectAs(Product::getCode, BomBO::getProductCode)
                .selectAs(Product::getName, BomBO::getProductName));
        Set<String> bomSet = bomList.stream()
                .map(bom -> bom.getProductCode() + "_" + bom.getVersion())
                .filter(key -> keyListMap.containsKey(new BomExcelBO.ProductVersionKey(key.split("_")[0], key.split("_")[1])))
                .collect(Collectors.toSet());
        if (!bomSet.isEmpty()) {
            throw new BusinessException("BOM【" + StringUtils.collectionToDelimitedString(bomSet, ",") + "】已存在！");
        }

        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .in(Product::getCode, productCodeSet));
        Map<String, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getCode, Product -> Product));
        // 查询物料信息
        Set<String> materialCodeSet = dataList.stream().map(BomExcelBO::getMaterialCode).collect(Collectors.toSet());
        List<Material> materialList = materialMapper.selectList(new LambdaQueryWrapper<Material>()
                .select(Material::getId, Material::getCode)
                .in(Material::getCode, materialCodeSet));
        Map<String, Material> materialMap = materialList.stream().collect(Collectors.toMap(Material::getCode, Material -> Material));

        // 保存数据
        List<BomDetail> bomDetailList = new ArrayList<>(dataList.size());
        for (Map.Entry<BomExcelBO.ProductVersionKey, List<BomExcelBO>> next : keyListMap.entrySet()) {
            BomExcelBO.ProductVersionKey key = next.getKey();
            List<BomExcelBO> value = next.getValue();
            // 创建BOM
            Bom bom = new Bom();
            Product product = productMap.get(key.getProductCode());
            bom.setCode(key.getProductCode() + "_" + key.getVersion());
            bom.setName(product.getName() + "_" + key.getVersion());
            bom.setVersion(key.getVersion());
            bom.setProductId(product.getId());
            bomMapper.insert(bom);
            // 创建BOM明细
            for (BomExcelBO bomExcelBO : value) {
                BomDetail bomDetail = new BomDetail();
                bomDetail.setBomId(bom.getId());
                Material material = materialMap.get(bomExcelBO.getMaterialCode());
                bomDetail.setMaterialId(material.getId());
                bomDetail.setQty(bomExcelBO.getQty());
                bomDetail.setParentId("0");
                bomDetailList.add(bomDetail);
            }
        }
        bomDetailService.saveBatch(bomDetailList);
        log.info("数据保存完成！");
    }

}
