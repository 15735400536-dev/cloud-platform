package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.CheckTemplateItemBO;
import com.maxinhai.platform.dto.CheckLabelQueryDTO;
import com.maxinhai.platform.enums.CheckType;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.*;
import com.maxinhai.platform.po.*;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.service.CheckLabelService;
import com.maxinhai.platform.utils.DocxUtils;
import com.maxinhai.platform.vo.CheckLabelVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName：CheckLabelServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/19 15:05
 * @Description: 电子履历标签业务实现类
 */
@Slf4j
@Service
public class CheckLabelServiceImpl extends ServiceImpl<CheckLabelMapper, CheckLabel> implements CheckLabelService {

    @Resource
    private CheckLabelMapper checkLabelMapper;
    @Resource
    private CheckTemplateItemRelMapper templateItemRelMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CheckOrderMapper checkOrderMapper;
    @Resource
    private CheckOrderDetailMapper checkOrderDetailMapper;
    @Resource
    private DocxUtils docxUtils;

    @Override
    public Page<CheckLabelVO> searchByPage(CheckLabelQueryDTO param) {
        return checkLabelMapper.selectJoinPage(param.getPage(), CheckLabelVO.class,
                new MPJLambdaWrapper<CheckLabel>()
                        .innerJoin(Product.class, Product::getId, CheckLabel::getProductId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(param.getProductId()), CheckLabel::getProductId, param.getProductId())
                        .eq(Objects.nonNull(param.getCheckType()) && !CheckType.ALL.equals(param.getCheckType()), CheckLabel::getCheckType, param.getCheckType())
                        // 字段别名
                        .selectAll(CheckLabel.class)
                        .selectAs(Product::getCode, CheckLabelVO::getProductCode)
                        .selectAs(Product::getName, CheckLabelVO::getProductName)
                        // 排序
                        .orderByAsc(CheckLabel::getCreateTime));
    }

    @Override
    public CheckLabelVO getInfo(String id) {
        return checkLabelMapper.selectJoinOne(CheckLabelVO.class, new MPJLambdaWrapper<CheckLabel>()
                .innerJoin(Product.class, Product::getId, CheckLabel::getProductId)
                // 查询条件
                .eq(StrUtil.isNotBlank(id), CheckLabel::getId, id)
                // 字段别名
                .selectAll(CheckLabel.class)
                .selectAs(Product::getCode, CheckLabelVO::getProductCode)
                .selectAs(Product::getName, CheckLabelVO::getProductName));
    }

    @Override
    public byte[] generate(String workOrderId) {
        Map<String, String> labelValueMap = getLabelValueMap(workOrderId);
        try {
            return docxUtils.generateDocx("电子履历模板.docx", labelValueMap);
        } catch (IOException e) {
            throw new BusinessException("生成电子履历失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, String> getLabelValueMap(String checkOrderId) {
        // 查询质检单
        CheckOrder checkOrder = checkOrderMapper.selectJoinOne(CheckOrder.class, new MPJLambdaWrapper<CheckOrder>()
                .innerJoin(Product.class, Product::getId, CheckOrder::getProductId)
                .innerJoin(Operation.class, Operation::getId, CheckOrder::getOperationId)
                .select(CheckOrder::getId, CheckOrder::getCheckType, CheckOrder::getCheckTemplateId, CheckOrder::getOperationId)
                .selectAs(Product::getCode, CheckOrder::getProductCode)
                .selectAs(Operation::getCode, CheckOrder::getOperationCode)
                .eq(CheckOrder::getId, checkOrderId));
        // 查询质检单明细
        List<CheckOrderDetail> checkOrderDetailList = checkOrderDetailMapper.selectList(new LambdaQueryWrapper<CheckOrderDetail>()
                .select(CheckOrderDetail::getId, CheckOrderDetail::getItemCode, CheckOrderDetail::getItemName,
                        CheckOrderDetail::getControlType, CheckOrderDetail::getMinValue, CheckOrderDetail::getMaxValue,
                        CheckOrderDetail::getCheckValue, CheckOrderDetail::getCheckResult)
                .eq(CheckOrderDetail::getCheckOrderId, checkOrder));
        // 查询产品数据
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode, Product::getName)
                .eq(Product::getId, checkOrder.getProductId()));
        // 装填数据
        Map<String, String> labelValueMap = new HashMap<>();
        for (CheckOrderDetail detail : checkOrderDetailList) {
            switch (detail.getControlType()) {
                case QL:
                    handleQualitative(product, checkOrder, detail, labelValueMap);
                    break;
                case QT:
                    handleQuantitative(product, checkOrder, detail, labelValueMap);
                    break;
                case MI:
                    handleManualInput(product, checkOrder, detail, labelValueMap);
                    break;
                default:
            }
        }
        return labelValueMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateLabel(String productId, CheckType checkType) {
        // 1.删除旧的电子履历标签
        checkLabelMapper.delete(new LambdaQueryWrapper<CheckLabel>()
                .eq(CheckLabel::getProductId, productId)
                .eq(Objects.nonNull(checkType), CheckLabel::getCheckType, checkLabelMapper));
        // 2.查询产品数据
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode, Product::getName)
                .eq(Product::getId, productId));
        // 3.根据产品ID和检测类型查询检测模板明细
        List<CheckTemplateItemBO> itemBOList = templateItemRelMapper.selectJoinList(CheckTemplateItemBO.class, new MPJLambdaWrapper<CheckTemplateItemRel>()
                .innerJoin(CheckTemplate.class, CheckTemplate::getId, CheckTemplateItemRel::getTemplateId)
                .innerJoin(CheckItem.class, CheckItem::getId, CheckTemplateItemRel::getItemId)
                .innerJoin(Operation.class, Operation::getId, CheckTemplate::getOperationId)
                // 查询条件
                .eq(CheckTemplate::getProductId, productId)
                .eq(Objects.nonNull(checkType), CheckTemplate::getCheckType, checkType)
                // 字段别名
                .selectAs(CheckTemplateItemRel::getTemplateId, CheckTemplateItemBO::getTemplateId)
                .selectAs(CheckTemplateItemRel::getItemId, CheckTemplateItemBO::getItemId)
                .selectAs(CheckTemplate::getCheckType, CheckTemplateItemBO::getCheckType)
                .selectAs(CheckTemplate::getOperationId, CheckTemplateItemBO::getOperationId)
                .selectAs(CheckItem::getItemCode, CheckTemplateItemBO::getItemCode)
                .selectAs(CheckItem::getItemName, CheckTemplateItemBO::getItemName)
                .selectAs(CheckItem::getControlType, CheckTemplateItemBO::getControlType)
                .selectAs(CheckItem::getMinValue, CheckTemplateItemBO::getMinValue)
                .selectAs(CheckItem::getMaxValue, CheckTemplateItemBO::getMaxValue)
                .selectAs(Operation::getCode, CheckTemplateItemBO::getOperationCode)
                // 排序
                .orderByAsc(CheckTemplateItemRel::getCreateTime));
        // 4.生成标签
        List<CheckLabel> labelList = new ArrayList<>();
        for (CheckTemplateItemBO itemBO : itemBOList) {
            switch (itemBO.getControlType()) {
                case QL:
                    handleQualitative(product, itemBO, labelList);
                    break;
                case QT:
                    handleQuantitative(product, itemBO, labelList);
                    break;
                case MI:
                    handleManualInput(product, itemBO, labelList);
                    break;
                default:
            }
        }
        this.saveBatch(labelList);
    }

    @Override
    public List<CheckLabel> getCheckLabelList(String productId, CheckType checkType) {
        return checkLabelMapper.selectList(new LambdaQueryWrapper<CheckLabel>()
                .select(CheckLabel::getId, CheckLabel::getCode, CheckLabel::getName, CheckLabel::getCheckType, CheckLabel::getProductId)
                .eq(CheckLabel::getProductId, productId)
                .eq(Objects.nonNull(checkType) && !CheckType.ALL.equals(checkType), CheckLabel::getCheckType, checkType));
    }

    // ======================================== 标签数据装填 ======================================== //

    /**
     * 处理定性检测项
     * @param product 产品信息
     * @param checkOrder 检测单
     * @param detail 检测单明细
     * @param labelValueMap 标签数值集合
     */
    private void handleQualitative(Product product, CheckOrder checkOrder, CheckOrderDetail detail, Map<String, String> labelValueMap) {
        // 编码规则：产品编码_检测类型_工序编码_检测项编码_字段
        // 检测结果
        String labelKey = product.getCode().toUpperCase() + "_" +
                checkOrder.getCheckType().getKey() + "_" +
                checkOrder.getOperationCode().toUpperCase() + "_" +
                detail.getItemCode().toUpperCase() + "_" +
                "check_result".toUpperCase();
        labelValueMap.put(labelKey, StrUtil.isEmpty(detail.getCheckResult()) ? "" : detail.getCheckResult());
    }

    /**
     * 处理定量检测项
     * @param product 产品信息
     * @param checkOrder 检测单
     * @param detail 检测单明细
     * @param labelValueMap 标签数值集合
     */
    private void handleQuantitative(Product product, CheckOrder checkOrder, CheckOrderDetail detail, Map<String, String> labelValueMap) {
        // 编码规则：产品编码_检测类型_工序编码_检测项编码_字段
        // 下限
        String minValueKey = product.getCode().toUpperCase() + "_" +
                checkOrder.getCheckType().getKey() + "_" +
                checkOrder.getOperationCode().toUpperCase() + "_" +
                detail.getItemCode().toUpperCase() + "_" +
                "min_value".toUpperCase();
        labelValueMap.put(minValueKey, StrUtil.isEmpty(detail.getCheckResult()) ? "" : detail.getMinValue().toString());
        // 上限
        String maxValueKey = product.getCode().toUpperCase() + "_" +
                checkOrder.getCheckType().getKey() + "_" +
                checkOrder.getOperationCode().toUpperCase() + "_" +
                detail.getItemCode().toUpperCase() + "_" +
                "max_value".toUpperCase();
        labelValueMap.put(maxValueKey, StrUtil.isEmpty(detail.getCheckResult()) ? "" : detail.getMaxValue().toString());
        // 检测数值
        String checkValueKey = product.getCode().toUpperCase() + "_" +
                checkOrder.getCheckType().getKey() + "_" +
                checkOrder.getOperationCode().toUpperCase() + "_" +
                detail.getItemCode().toUpperCase() + "_" +
                "check_value".toUpperCase();
        labelValueMap.put(checkValueKey, StrUtil.isEmpty(detail.getCheckResult()) ? "" : detail.getCheckValue().toString());
        // 检测结果
        String checkResultKey = product.getCode().toUpperCase() + "_" +
                checkOrder.getCheckType().getKey() + "_" +
                checkOrder.getOperationCode().toUpperCase() + "_" +
                detail.getItemCode().toUpperCase() + "_" +
                "check_result".toUpperCase();
        labelValueMap.put(checkResultKey, StrUtil.isEmpty(detail.getCheckResult()) ? "" : detail.getCheckResult());
    }

    /**
     * 处理手动输入检测项
     */
    private void handleManualInput(Product product, CheckOrder checkOrder, CheckOrderDetail detail, Map<String, String> labelValueMap) {
        // 编码规则：产品编码_检测类型_工序编码_检测项编码_字段
        // 检测结果
        String labelKey = product.getCode().toUpperCase() + "_" +
                checkOrder.getCheckType().getKey() + "_" +
                checkOrder.getOperationCode().toUpperCase() + "_" +
                detail.getItemCode().toUpperCase() + "_" +
                "check_result".toUpperCase();
        labelValueMap.put(labelKey, StrUtil.isEmpty(detail.getCheckResult()) ? "" : detail.getCheckResult());
    }

    // ======================================== 标签生成 ======================================== //

    /**
     * 处理定性检测项
     *
     * @param product   产品信息
     * @param itemBO    检测项信息
     * @param labelList 标签集合
     */
    private void handleQualitative(Product product, CheckTemplateItemBO itemBO, List<CheckLabel> labelList) {
        // 编码规则：产品编码_检测类型_工序编码_检测项编码_字段
        String checkResult = product.getCode().toUpperCase() + "_" +
                itemBO.getCheckType().getKey() + "_" +
                itemBO.getOperationCode().toUpperCase() + "_" +
                itemBO.getItemCode().toUpperCase() + "_" +
                "check_result".toUpperCase();
        CheckLabel checkResultLabel = new CheckLabel();
        checkResultLabel.setCode(checkResult);
        checkResultLabel.setName("检测结果");
        checkResultLabel.setProductId(product.getId());
        checkResultLabel.setCheckType(itemBO.getCheckType());
        labelList.add(checkResultLabel);
    }

    /**
     * 处理定量检测项
     *
     * @param product   产品信息
     * @param itemBO    检测项信息
     * @param labelList 标签集合
     */
    private void handleQuantitative(Product product, CheckTemplateItemBO itemBO, List<CheckLabel> labelList) {
        // 编码规则：产品编码_检测类型_工序编码_检测项编码_字段
        String checkValue = product.getCode().toUpperCase() + "_" +
                itemBO.getCheckType().getKey() + "_" +
                itemBO.getOperationCode().toUpperCase() + "_" +
                itemBO.getItemCode().toUpperCase() + "_" +
                "check_value".toUpperCase();
        CheckLabel checkValueLabel = new CheckLabel();
        checkValueLabel.setCode(checkValue);
        checkValueLabel.setName("检测值");
        checkValueLabel.setProductId(product.getId());
        checkValueLabel.setCheckType(itemBO.getCheckType());
        labelList.add(checkValueLabel);

        String minValue = product.getCode().toUpperCase() + "_" +
                itemBO.getCheckType().getKey() + "_" +
                itemBO.getOperationCode().toUpperCase() + "_" +
                itemBO.getItemCode().toUpperCase() + "_" +
                "min_value".toUpperCase();
        CheckLabel minValueLabel = new CheckLabel();
        minValueLabel.setCode(minValue);
        minValueLabel.setName("下限");
        minValueLabel.setProductId(product.getId());
        minValueLabel.setCheckType(itemBO.getCheckType());
        labelList.add(minValueLabel);

        String maxValue = product.getCode().toUpperCase() + "_" +
                itemBO.getCheckType().getKey() + "_" +
                itemBO.getOperationCode().toUpperCase() + "_" +
                itemBO.getItemCode().toUpperCase() + "_" +
                "max_value".toUpperCase();
        CheckLabel maxValueLabel = new CheckLabel();
        maxValueLabel.setCode(maxValue);
        maxValueLabel.setName("上限");
        maxValueLabel.setProductId(product.getId());
        maxValueLabel.setCheckType(itemBO.getCheckType());
        labelList.add(maxValueLabel);

        String checkResult = product.getCode().toUpperCase() + "_" +
                itemBO.getCheckType().getKey() + "_" +
                itemBO.getOperationCode().toUpperCase() + "_" +
                itemBO.getItemCode().toUpperCase() + "_" +
                "check_result".toUpperCase();
        CheckLabel checkResultLabel = new CheckLabel();
        checkResultLabel.setCode(checkResult);
        checkResultLabel.setName("检测结果");
        checkResultLabel.setProductId(product.getId());
        checkResultLabel.setCheckType(itemBO.getCheckType());
        labelList.add(checkResultLabel);
    }

    /**
     * 处理手动输入检测项
     *
     * @param product   产品信息
     * @param itemBO    检测项信息
     * @param labelList 标签集合
     */
    private void handleManualInput(Product product, CheckTemplateItemBO itemBO, List<CheckLabel> labelList) {
        // 编码规则：产品编码_检测类型_工序编码_检测项编码_字段
        String checkResult = product.getCode().toUpperCase() + "_" +
                itemBO.getCheckType().getKey() + "_" +
                itemBO.getOperationCode().toUpperCase() + "_" +
                itemBO.getItemCode().toUpperCase() + "_" +
                "check_result".toUpperCase();
        CheckLabel checkResultLabel = new CheckLabel();
        checkResultLabel.setCode(checkResult);
        checkResultLabel.setName("检测结果");
        checkResultLabel.setProductId(product.getId());
        checkResultLabel.setCheckType(itemBO.getCheckType());
        labelList.add(checkResultLabel);
    }
}
