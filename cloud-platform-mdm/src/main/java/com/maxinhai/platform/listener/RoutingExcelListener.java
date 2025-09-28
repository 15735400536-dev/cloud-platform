package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.BomBO;
import com.maxinhai.platform.bo.RoutingBO;
import com.maxinhai.platform.bo.RoutingExcelBO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.OperationMapper;
import com.maxinhai.platform.mapper.ProductMapper;
import com.maxinhai.platform.mapper.RoutingMapper;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.po.technology.Routing;
import com.maxinhai.platform.po.technology.RoutingOperationRel;
import com.maxinhai.platform.service.RoutingOperationRelService;
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
public class RoutingExcelListener implements ReadListener<RoutingExcelBO> {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private RoutingMapper routingMapper;
    @Resource
    private OperationMapper operationMapper;
    @Resource
    private RoutingOperationRelService relService;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private final List<RoutingExcelBO> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(RoutingExcelBO data, AnalysisContext context) {
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
        Map<RoutingExcelBO.ProductVersionKey, List<RoutingExcelBO>> keyListMap = RoutingExcelBO.groupByProductAndVersion(dataList);

        // 查询产品信息
        Set<String> productCodeSet = dataList.stream().map(RoutingExcelBO::getProductCode).collect(Collectors.toSet());

        // 校验工艺路线是否存在
        List<RoutingBO> routingList = routingMapper.selectJoinList(RoutingBO.class, new MPJLambdaWrapper<Routing>()
                .innerJoin(Product.class, Product::getId, Bom::getProductId)
                .in(Product::getCode, productCodeSet)
                .select(Routing::getVersion, Routing::getProductId)
                .select(Product::getCode, Product::getName)
                .selectAll(Routing.class)
                .selectAs(Product::getCode, BomBO::getProductCode)
                .selectAs(Product::getName, BomBO::getProductName));
        Set<String> routingSet = routingList.stream()
                .map(routing -> routing.getProductCode() + "_" + routing.getVersion())
                .filter(key -> keyListMap.containsKey(new RoutingExcelBO.ProductVersionKey(key.split("_")[0], key.split("_")[1])))
                .collect(Collectors.toSet());
        if (!routingSet.isEmpty()) {
            throw new BusinessException("工艺路线【" + StringUtils.collectionToDelimitedString(routingSet, ",") + "】已存在！");
        }

        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .in(Product::getCode, productCodeSet));
        Map<String, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getCode, Product -> Product));
        // 查询工序信息
        Set<String> operationCodeSet = dataList.stream().map(RoutingExcelBO::getOperationCode).collect(Collectors.toSet());
        List<Operation> operationList = operationMapper.selectList(new LambdaQueryWrapper<Operation>()
                .select(Operation::getId, Operation::getCode, Operation::getName)
                .in(Operation::getCode, operationCodeSet));
        Map<String, Operation> operationMap = operationList.stream().collect(Collectors.toMap(Operation::getCode, Operation -> Operation));

        // 保存数据
        List<RoutingOperationRel> relList = new ArrayList<>(dataList.size());
        for (Map.Entry<RoutingExcelBO.ProductVersionKey, List<RoutingExcelBO>> next : keyListMap.entrySet()) {
            RoutingExcelBO.ProductVersionKey key = next.getKey();
            List<RoutingExcelBO> value = next.getValue();
            // 创建工艺路线
            Product product = productMap.get(key.getProductCode());
            Routing routing = new Routing();
            routing.setCode(product.getCode() + "_" + key.getVersion());
            routing.setName(product.getName() + "_" + key.getVersion());
            routing.setProductId(product.getId());
            routing.setVersion(key.getVersion());
            routing.setStatus(1);
            routingMapper.insert(routing);
            // 创建工序
            for (int i = 0; i < value.size(); i++) {
                RoutingExcelBO excelBO = value.get(i);

                Operation operation = null;
                if (operationMap.containsKey(excelBO.getOperationCode())) {
                    operation = operationMap.get(excelBO.getOperationCode());
                } else {
                    operation = new Operation();
                    operation.setCode(excelBO.getOperationCode());
                    operation.setName(excelBO.getOperationName());
                    operation.setWorkTime(excelBO.getWorkTime());
                    operation.setStatus(1);
                    operationMapper.insert(operation);
                }

                // 绑定关系
                RoutingOperationRel rel = new RoutingOperationRel(routing.getId(), operation.getId(), i + 1);
                relList.add(rel);
            }
        }
        relService.saveBatch(relList);
        log.info("数据保存完成！");
    }

}
