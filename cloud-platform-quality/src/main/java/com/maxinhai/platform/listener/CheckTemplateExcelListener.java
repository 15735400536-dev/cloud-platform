package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.bo.CheckTemplateExcel;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.*;
import com.maxinhai.platform.po.CheckItem;
import com.maxinhai.platform.po.CheckTemplate;
import com.maxinhai.platform.po.CheckTemplateItemRel;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.service.CheckTemplateItemRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CheckTemplateExcelListener implements ReadListener<CheckTemplateExcel> {

    @Lazy
    @Resource
    private CheckTemplateMapper templateMapper;
    @Resource
    private CheckItemMapper itemMapper;
    @Resource
    private CheckTemplateItemRelService templateItemRelService;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OperationMapper operationMapper;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private List<CheckTemplateExcel> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(CheckTemplateExcel data, AnalysisContext context) {
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
    private void saveData() {
        // 没有内容不执行后面操作
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        log.info("开始保存 {} 条数据到数据库", dataList.size());
        // 保存数据
        Set<String> templateCodeSet = dataList.stream().map(CheckTemplateExcel::getTemplateCode).collect(Collectors.toSet());
        Set<String> itemCodeSet = dataList.stream().map(CheckTemplateExcel::getItemCode).collect(Collectors.toSet());
        List<CheckTemplate> templateList = templateMapper.selectList(new LambdaQueryWrapper<CheckTemplate>()
                .select(CheckTemplate::getId, CheckTemplate::getTemplateCode)
                .in(CheckTemplate::getTemplateCode, templateCodeSet));
        if (!templateList.isEmpty()) {
            Set<String> codeSet = templateList.stream().map(CheckTemplate::getTemplateCode).collect(Collectors.toSet());
            throw new BusinessException("检测模板【" + StringUtils.collectionToDelimitedString(codeSet, ",") + "】已存在！");
        }
        List<CheckItem> itemList = itemMapper.selectList(new LambdaQueryWrapper<CheckItem>()
                .select(CheckItem::getId, CheckItem::getItemCode)
                .in(CheckItem::getItemCode, itemCodeSet));
        if (!itemList.isEmpty()) {
            Set<String> codeSet = itemList.stream().map(CheckItem::getItemCode).collect(Collectors.toSet());
            throw new BusinessException("检测项【" + StringUtils.collectionToDelimitedString(codeSet, ",") + "】已存在！");
        }

        // 查询产品信息
        Set<String> productCodeSet = dataList.stream().map(CheckTemplateExcel::getProductCode).collect(Collectors.toSet());
        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode, Product::getName)
                .in(Product::getCode, productCodeSet));
        Map<String, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getId, Product -> Product));
        // 查询工序信息
        Set<String> operationCodeSet = dataList.stream().map(CheckTemplateExcel::getOperationCode).collect(Collectors.toSet());
        List<Operation> operationList = operationMapper.selectList(new LambdaQueryWrapper<Operation>()
                .select(Operation::getId, Operation::getCode, Operation::getName)
                .in(Operation::getCode, operationCodeSet));
        Map<String, Operation> operationMap = operationList.stream().collect(Collectors.toMap(Operation::getId, Operation -> Operation));

        List<CheckTemplateItemRel> relList = new ArrayList<>(dataList.size());
        Map<String, CheckTemplate> checkTemplateMap = new HashMap<>(templateCodeSet.size());
        for (CheckTemplateExcel excel : dataList) {
            // 检测模板
            CheckTemplate template = null;
            if (!checkTemplateMap.containsKey(excel.getTemplateCode())) {
                template = CheckTemplateExcel.buildCheckTemplate(excel);
                if (productMap.containsKey(excel.getProductCode())) {
                    throw new BusinessException("产品【" + excel.getProductCode() + "】未找到！");
                }
                Product product = productMap.get(excel.getProductCode());
                template.setProductId(product.getId());

                if (operationMap.containsKey(excel.getOperationCode())) {
                    throw new BusinessException("工序【" + excel.getOperationCode() + "】未找到！");
                }
                Operation operation = operationMap.get(excel.getOperationCode());
                template.setOperationId(operation.getId());
                templateMapper.insert(template);
                checkTemplateMap.put(excel.getTemplateCode(), template);
            } else {
                template = checkTemplateMap.get(excel.getTemplateCode());
            }

            // 检测项
            CheckItem item = CheckTemplateExcel.buildCheckItem(excel);
            itemMapper.insert(item);

            // 绑定关联
            CheckTemplateItemRel rel = new CheckTemplateItemRel(template.getId(), item.getId());
            relList.add(rel);
        }
        templateItemRelService.saveBatch(relList);
        log.info("数据保存完成！");
    }

}
