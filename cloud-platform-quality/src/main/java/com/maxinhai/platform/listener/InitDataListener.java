package com.maxinhai.platform.listener;

import com.maxinhai.platform.enums.CheckType;
import com.maxinhai.platform.mapper.*;
import com.maxinhai.platform.po.CheckTemplate;
import com.maxinhai.platform.po.CheckTemplateItemRel;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.po.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class InitDataListener implements CommandLineRunner {

    @Resource
    private CheckItemMapper itemMapper;
    @Resource
    private CheckTemplateMapper templateMapper;
    @Resource
    private CheckTemplateItemRelMapper templateItemRelMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OperationMapper operationMapper;

    @Override
    public void run(String... args) throws Exception {
//        List<String> itemIds = new ArrayList<>();
//        for (int i = 1; i <= 10; i++) {
//            CheckItem item = new CheckItem();
//            item.setItemCode("code" + i);
//            item.setItemName("name" + i);
//            item.setControlType(ControlType.QT);
//            item.setMinValue(BigDecimal.TEN);
//            item.setMaxValue(BigDecimal.ONE);
//            itemMapper.insert(item);
//            itemIds.add(item.getId());
//        }
//
//        List<Product> productList = productMapper.selectList(null);
//        List<Operation> operationList = operationMapper.selectList(null);
//
//        for (Product product : productList) {
//            for (Operation operation : operationList) {
//                createSelfCheck(product, operation, itemIds);
//                createMutualCheck(product, operation, itemIds);
//                createSpecialCheck(product, operation, itemIds);
//            }
//        }
    }

    /**
     * 生成自检模板
     * @param product
     * @param operation
     * @param itemIds
     */
    private void createSelfCheck(Product product, Operation operation, List<String> itemIds) {
        CheckTemplate template = new CheckTemplate();
        template.setTemplateCode(product.getCode() + "_" + operation.getCode() + "_SELF_CHECK");
        template.setTemplateName(product.getName() + "_" + operation.getName() + "_自检");
        template.setCheckType(CheckType.SELF_CHECK);
        template.setProductId(product.getId());
        template.setOperationId(operation.getId());
        templateMapper.insert(template);

        for (String itemId : itemIds) {
            CheckTemplateItemRel rel = new CheckTemplateItemRel();
            rel.setTemplateId(template.getId());
            rel.setItemId(itemId);
            templateItemRelMapper.insert(rel);
        }
    }

    /**
     * 生成互检模板
     * @param product
     * @param operation
     * @param itemIds
     */
    private void createMutualCheck(Product product, Operation operation, List<String> itemIds) {
        CheckTemplate template = new CheckTemplate();
        template.setTemplateCode(product.getCode() + "_" + operation.getCode() + "_MUTUAL_CHECK");
        template.setTemplateName(product.getName() + "_" + operation.getName() + "_互检");
        template.setCheckType(CheckType.MUTUAL_CHECK);
        template.setProductId(product.getId());
        template.setOperationId(operation.getId());
        templateMapper.insert(template);

        for (String itemId : itemIds) {
            CheckTemplateItemRel rel = new CheckTemplateItemRel();
            rel.setTemplateId(template.getId());
            rel.setItemId(itemId);
            templateItemRelMapper.insert(rel);
        }
    }

    /**
     * 生成专检模板
     * @param product
     * @param operation
     * @param itemIds
     */
    private void createSpecialCheck(Product product, Operation operation, List<String> itemIds) {
        CheckTemplate template = new CheckTemplate();
        template.setTemplateCode(product.getCode() + "_" + operation.getCode() + "_SPECIAL_CHECK");
        template.setTemplateName(product.getName() + "_" + operation.getName() + "_专检");
        template.setCheckType(CheckType.SPECIAL_CHECK);
        template.setProductId(product.getId());
        template.setOperationId(operation.getId());
        templateMapper.insert(template);

        for (String itemId : itemIds) {
            CheckTemplateItemRel rel = new CheckTemplateItemRel();
            rel.setTemplateId(template.getId());
            rel.setItemId(itemId);
            templateItemRelMapper.insert(rel);
        }
    }

}
